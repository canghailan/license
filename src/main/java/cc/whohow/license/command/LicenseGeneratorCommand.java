package cc.whohow.license.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(resourceBundle = "command", commandDescriptionKey = "LicenseGenerator")
public class LicenseGeneratorCommand {
    @Parameter(names = {"-k", "--key"}, descriptionKey = "key")
    private String key = "private.pem";
    @Parameter(names = {"-d", "--data"}, descriptionKey = "data")
    private String data = "license.yml";
    @Parameter(descriptionKey = "output")
    private String output = "license";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
