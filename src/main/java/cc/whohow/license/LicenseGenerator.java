package cc.whohow.license;

import cc.whohow.license.util.IO;
import cc.whohow.license.util.Pem;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.*;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class LicenseGenerator implements Runnable {
    private PrivateKey key;
    private Map<String, String> data = new LinkedHashMap<>();
    private String output = "license.dat";

    public PrivateKey getKey() {
        return key;
    }

    public void setKey(URL key) {
        setKey(new Pem(key));
    }

    public void setKey(String key) {
        setKey(new Pem(key));
    }

    public void setKey(Pem key) {
        try {
            setKey(KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(key.getData())));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    public void setKey(PrivateKey key) {
        this.key = key;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public void addData(Map<String, String> data) {
        this.data.putAll(data);
    }

    public void addData(String key, String value) {
        this.data.put(key, value);
    }

    @Override
    public void run() {
        JwtBuilder builder = Jwts.builder();
        builder.setIssuedAt(new Date());
        for (Map.Entry<String, String> e: data.entrySet()) {
            builder.claim(e.getKey(), e.getValue());
        }

        IO.writeAscii(new File(output), builder.signWith(SignatureAlgorithm.RS512, key).compact());
    }
}
