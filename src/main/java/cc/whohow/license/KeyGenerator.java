package cc.whohow.license;

import cc.whohow.license.util.IO;
import cc.whohow.license.util.Pem;

import java.io.*;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class KeyGenerator implements Runnable {
    private String algorithm = "RSA";
    private int keySize = 2048;
    private String output = ".";

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public void run() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            Pem publicKey = new Pem("RSA PUBLIC KEY", keyPair.getPublic().getEncoded());
            Pem privateKey = new Pem("RSA PRIVATE KEY", keyPair.getPrivate().getEncoded());

            File file = new File(output);
            if (file.exists() && file.isDirectory()) {
                IO.writeAscii(new File(file, "public.pem"), publicKey.toString());
                IO.writeAscii(new File(file, "private.pem"), privateKey.toString());
            } else {
                try (ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(file))) {
                    zip.putNextEntry(new ZipEntry("public.pem"));
                    zip.setComment(publicKey.getLabel());
                    zip.write(publicKey.toString().getBytes(StandardCharsets.US_ASCII));
                    zip.closeEntry();
                    zip.putNextEntry(new ZipEntry("private.pem"));
                    zip.setComment(privateKey.getLabel());
                    zip.write(privateKey.toString().getBytes(StandardCharsets.US_ASCII));
                    zip.closeEntry();
                }
            }
        } catch (NoSuchAlgorithmException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
