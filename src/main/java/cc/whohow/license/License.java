package cc.whohow.license;

import cc.whohow.license.util.IO;
import cc.whohow.license.util.Pem;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class License {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("license");

    private String key;
    private String license;

    public License(URL key, URL license) {
        this(IO.readAscii(key), IO.readAscii(license));
    }

    public License(File key, File license) {
        this(IO.readAscii(key), IO.readAscii(license));
    }

    public License(String key, String license) {
        this.key = key;
        this.license = license;
    }

    public Map<String, Object> getData() {
        return parse().getBody();
    }

    private static JsonNode parseJwt(String jwt) {
        try {
            return new ObjectMapper().readTree(Base64.getUrlDecoder().decode(jwt.split("\\.")[1]));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    private static void validateCPU(Claims data, HardwareAbstractionLayer hardware) {
        String expected = data.get("hw-cpu", String.class);
        String actual = hardware.getProcessor().getProcessorID();
        if (expected == null || expected.equals(actual)) {
            return;
        }
        throw new LicenseInvalidateException("hw-cpu", expected, actual);
    }

    private static void validateMotherboard(Claims data, HardwareAbstractionLayer hardware) {
        String expected = data.get("hw-mobo", String.class);
        String actual = hardware.getComputerSystem().getBaseboard().getSerialNumber();
        if (expected == null || expected.equals(actual)) {
            return;
        }
        throw new LicenseInvalidateException("hw-mobo", expected, actual);
    }

    private static void validateMAC(Claims data, HardwareAbstractionLayer hardware) {
        String expected = data.get("hw-mac", String.class);
        if (expected == null) {
            return;
        }
        List<String> actualList = new ArrayList<>();
        for (NetworkIF network : hardware.getNetworkIFs()) {
            String actual = network.getMacaddr();
            if (expected.equals(actual)) {
                return;
            }
            actualList.add(actual);
        }
        throw new LicenseInvalidateException("hw-mac", expected, String.join(", ", actualList));
    }

    private Jws<Claims> parse() {
        try {
            return Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(license);
        } catch (ExpiredJwtException e) {
            throw new LicenseInvalidateException("exp", "Expired", e);
        }
    }

    private PublicKey getPublicKey() {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(new Pem(key).getData()));
        } catch (NoSuchAlgorithmException e) {
            throw new UndeclaredThrowableException(e);
        } catch (InvalidKeySpecException e) {
            throw new LicenseInvalidateException("InvalidKey", e);
        }
    }

    public void validate() {
        Claims data = parse().getBody();
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        validateCPU(data, hardware);
        validateMotherboard(data, hardware);
        validateMAC(data, hardware);
    }

    public void check() {
        try {
            validate();
        } catch (LicenseInvalidateException e) {
            if (e.getKey() != null) {
                if (e.getExpectedValue() != null || e.getActualValue() != null) {
                    System.err.println(RESOURCE_BUNDLE.getString(e.getKey()) + ": " +
                            e.getExpectedValue() + " <> " + e.getActualValue());
                } else {
                    System.err.println(RESOURCE_BUNDLE.getString(e.getKey()) + " X");
                }
            } else {
                System.err.println(e.getMessage());
            }
            System.err.println("<<<<<<<<<<");
            System.err.print(this);
            System.err.println(">>>>>>>>>>");
            System.exit(-1);
        }
    }

    public void check(float rate) {
        if (ThreadLocalRandom.current().nextFloat() < rate) {
            check();
        }
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        JsonNode body = parseJwt(license);
        Iterator<String> fieldNames = body.fieldNames();
        while (fieldNames.hasNext()) {
            String name = fieldNames.next();
            buffer.append(RESOURCE_BUNDLE.getString(name)).append(": ");
            if ("iat".equals(name) || "exp".equals(name)) {
                buffer.append(formatDate(new Date(body.path(name).longValue())));
            } else {
                buffer.append(body.path(name).asText());
            }
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
