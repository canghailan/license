package cc.whohow.license;

import cc.whohow.license.util.IO;
import oshi.json.SystemInfo;

import java.io.File;

public class HardwareDetector implements Runnable {
    private String output = "hardware.json";

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public void run() {
        IO.writeUtf8(new File(output), new SystemInfo().getHardware().toCompactJSON());
    }
}
