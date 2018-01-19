package cc.whohow.license.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(resourceBundle = "command", commandDescriptionKey = "HardwareDetector")
public class HardwareDetectorCommand {
    @Parameter(descriptionKey = "output")
    private String output = "hardware.json";

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
