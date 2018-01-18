package cc.whohow.license;

import cc.whohow.license.util.IO;
import cc.whohow.license.util.Pem;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import oshi.json.SystemInfo;

import javax.json.JsonObject;
import java.io.File;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public final class License {
    private final String key;
    private final String license;

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

    public Jws<Claims> parse() {
        return Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(license);
    }

    public PublicKey getPublicKey() {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(new Pem(key).getData()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    public JsonObject getHardware() {
        return new SystemInfo().getHardware().toJSON();
    }

    public boolean isValid() {
        Jws<Claims> token = parse();
        JsonObject hardware = getHardware();
        System.out.println(token);
        System.out.println(hardware);
        return true;
    }

    public void check() {
        if (!isValid()) {
            System.err.println("Invalidate License:\n" + toString());
            System.exit(-1);
        }
    }

    @Override
    public String toString() {
        return parse().toString();
    }
}
