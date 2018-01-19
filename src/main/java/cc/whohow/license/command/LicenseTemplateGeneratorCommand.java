package cc.whohow.license.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(resourceBundle = "command", commandDescriptionKey = "LicenseTemplateGenerator")
public class LicenseTemplateGeneratorCommand {
    @Parameter(names = {"-w", "--hardware"}, descriptionKey = "hardware")
    private String hardware = "hardware.json";
    @Parameter(descriptionKey = "output")
    private String output = "license.yml";

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
