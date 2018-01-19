package cc.whohow.license;

import cc.whohow.license.command.HardwareDetector;
import org.junit.Test;

public class TestHardwareDetector {
    @Test
    public void test() {
        HardwareDetector hardwareDetector = new HardwareDetector();
        hardwareDetector.run();
    }
}
