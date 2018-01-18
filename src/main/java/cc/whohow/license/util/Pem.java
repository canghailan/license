package cc.whohow.license.util;

import java.io.File;
import java.net.URL;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pem {
    public static final Pattern PEM = Pattern.compile("(?<begin>-----BEGIN (?<label>[^-]+)-----)(?<data>.*)(?<end>-----END (?<endLabel>[^-]+)-----)\\s*", Pattern.DOTALL);

    private final String label;
    private final byte[] data;

    public Pem(String pem) {
        Matcher matcher = PEM.matcher(pem);
        if (matcher.matches()) {
            this.label = matcher.group("label");
            if (!label.equals(matcher.group("endLabel"))) {
                throw new IllegalArgumentException(pem);
            }
            this.data = Base64.getMimeDecoder().decode(matcher.group("data"));
        } else {
            throw new IllegalArgumentException(pem);
        }
    }

    public Pem(URL pem) {
        this(IO.readAscii(pem));
    }

    public Pem(File pem) {
        this(IO.readAscii(pem));
    }

    public Pem(String label, byte[] data) {
        this.label = label;
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "-----BEGIN " + label + "-----\n" + Base64.getMimeEncoder().encodeToString(data) + "\n-----END " + label + "-----\n";
    }
}
