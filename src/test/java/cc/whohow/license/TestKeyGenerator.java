package cc.whohow.license;

import org.junit.Test;

public class TestKeyGenerator {
    @Test
    public void test() {
        KeyGenerator keyGenerator = new KeyGenerator();
        keyGenerator.setOutput("a.zip");
        keyGenerator.run();
        keyGenerator.setOutput(".");
        keyGenerator.run();
    }
}
