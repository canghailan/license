package cc.whohow.license.command;

import cc.whohow.license.util.Pem;
import com.beust.jcommander.JCommander;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        KeyGeneratorCommand keyGeneratorCommand = new KeyGeneratorCommand();
        HardwareDetectorCommand hardwareDetectorCommand = new HardwareDetectorCommand();
        LicenseTemplateGeneratorCommand licenseTemplateGeneratorCommand = new LicenseTemplateGeneratorCommand();
        LicenseGeneratorCommand licenseGeneratorCommand = new LicenseGeneratorCommand();

        JCommander commander = JCommander.newBuilder()
                .addCommand("keygen", keyGeneratorCommand)
                .addCommand("hardware", hardwareDetectorCommand)
                .addCommand("template", licenseTemplateGeneratorCommand)
                .addCommand("license", licenseGeneratorCommand)
                .build();
        commander.parse(args);

        if (commander.getParsedCommand() == null) {
            commander.usage();
            return;
        }
        switch (commander.getParsedCommand()) {
            case "keygen": {
                KeyGenerator keyGenerator = new KeyGenerator();
                keyGenerator.setAlgorithm(keyGeneratorCommand.getAlgorithm());
                keyGenerator.setKeySize(keyGeneratorCommand.getKeySize());
                keyGenerator.setOutput(keyGenerator.getOutput());
                keyGenerator.run();
                return;
            }
            case "hardware" : {
                HardwareDetector hardwareDetector = new HardwareDetector();
                hardwareDetector.setOutput(hardwareDetectorCommand.getOutput());
                hardwareDetector.run();
                return;
            }
            case "template": {
                LicenseTemplateGenerator licenseTemplateGenerator = new LicenseTemplateGenerator();
                File file = new File(licenseTemplateGeneratorCommand.getHardware());
                if (file.exists()) {
                    licenseTemplateGenerator.setHardware(new ObjectMapper().readTree(file));
                }
                licenseTemplateGenerator.setOutput(licenseTemplateGeneratorCommand.getOutput());
                licenseTemplateGenerator.run();
                return;
            }
            case "license": {
                LicenseGenerator licenseGenerator = new LicenseGenerator();
                licenseGenerator.setKey(new Pem(new File(licenseGeneratorCommand.getKey())));
                licenseGenerator.setData(new ObjectMapper(new YAMLFactory()).readValue(
                                new File(licenseGeneratorCommand.getData()),
                                new TypeReference<Map<String, String>>(){}));
                licenseGenerator.setOutput(licenseGenerator.getOutput());
                licenseGenerator.run();
                return;
            }
            default: {
                commander.usage();
                break;
            }
        }
    }
}
