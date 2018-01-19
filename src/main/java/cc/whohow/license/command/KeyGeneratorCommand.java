package cc.whohow.license.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(resourceBundle = "command", commandDescriptionKey = "KeyGenerator")
public class KeyGeneratorCommand {
    @Parameter(names = {"-a", "--algorithm"}, descriptionKey = "algorithm")
    private String algorithm = "RSA";
    @Parameter(names = {"-s", "--size"}, descriptionKey = "keySize")
    private int keySize = 2048;
    @Parameter(descriptionKey = "output")
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
}
