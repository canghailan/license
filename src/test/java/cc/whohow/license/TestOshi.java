package cc.whohow.license;

import org.junit.Test;
import oshi.json.SystemInfo;

public class TestOshi {
    @Test
    public void testOshiJson() {
        System.out.println(new SystemInfo().getHardware().toPrettyJSON());
    }
}
