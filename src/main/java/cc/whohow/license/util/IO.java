package cc.whohow.license.util;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class IO {
    public static byte[] readBytes(URL url) {
        try (Jdk9InputStream stream = new Jdk9InputStream(url.openStream())) {
            return stream.readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String readAscii(URL url) {
        return new String(readBytes(url), StandardCharsets.US_ASCII);
    }

    public static byte[] readBytes(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String readAscii(File file) {
        return new String(readBytes(file), StandardCharsets.US_ASCII);
    }

    public static void writeBytes(File file, byte[] bytes) {
        try {
            Files.write(file.toPath(), bytes, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void writeAscii(File file, String ascii) {
        writeBytes(file, ascii.getBytes(StandardCharsets.US_ASCII));
    }

    public static void writeUtf8(File file, String text) {
        writeBytes(file, text.getBytes(StandardCharsets.UTF_8));
    }
}
