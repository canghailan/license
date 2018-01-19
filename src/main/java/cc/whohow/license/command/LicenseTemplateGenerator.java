package cc.whohow.license.command;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class LicenseTemplateGenerator implements Runnable {
    private JsonNode hardware;
    private String output = "license.yml";

    public JsonNode getHardware() {
        return hardware;
    }

    public void setHardware(JsonNode hardware) {
        this.hardware = hardware;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public void run() {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(output), StandardCharsets.UTF_8)))) {
            Date iat = new Date();
            ResourceBundle resource = ResourceBundle.getBundle("license");
            writer.printf("# %s\n", resource.getString("iss"));
            writer.println("iss: ");
            writer.printf("# %s\n", resource.getString("sub"));
            writer.println("sub: ");
            writer.printf("# %s\n", resource.getString("exp"));
            writer.printf("exp: \"%1$tF %1$tT\"\n", new Date(iat.getTime() + TimeUnit.DAYS.toMillis(365)));
            writer.printf("# %s\n", resource.getString("iat"));
            writer.printf("iat: \"%1$tF %1$tT\"\n", iat);
            writer.printf("# %s\n", resource.getString("hw-cpu"));
            writer.printf("hw-cpu: \"%s\"\n", getCPU());
            writer.printf("# %s\n", resource.getString("hw-mobo"));
            writer.printf("hw-mobo: \"%s\"\n", getMotherboard());
            writer.printf("# %s\n", resource.getString("hw-mac"));
            writer.printf("hw-mac: \"%s\"\n", getMAC());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String getCPU() {
        if (hardware == null) {
            return "";
        }
        return hardware.at("/processor/processorID").asText("");
    }

    public String getMotherboard() {
        if (hardware == null) {
            return "";
        }
        return hardware.at("/computerSystem/baseboard/serialNumber").asText("");
    }

    public String getMAC() {
        if (hardware == null) {
            return "";
        }
        TreeMap<Long, String> mac = new TreeMap<>();
        for (JsonNode network : hardware.path("networks")) {
            mac.put(network.path("bytesSent").asLong(0L), network.path("mac").asText(""));
        }
        return mac.lastEntry().getValue();
    }
}
