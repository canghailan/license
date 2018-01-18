package cc.whohow.license;

import cc.whohow.license.util.Pem;
import org.junit.Test;

import java.io.File;

public class TestLicense {
    @Test
    public void testLicenseGenerator() throws Exception {
        Pem privateKey = new Pem(new File("private.pem"));

        LicenseGenerator licenseGenerator = new LicenseGenerator();
        licenseGenerator.setKey(privateKey);
        licenseGenerator.addData("test", "test");
        licenseGenerator.run();
    }
    @Test
    public void testLicense() throws Exception {
        License license = new License(new File("public.pem"), new File("license.dat"));
        license.check();
    }
}
