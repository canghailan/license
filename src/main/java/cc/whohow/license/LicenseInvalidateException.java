package cc.whohow.license;

public class LicenseInvalidateException extends RuntimeException {
    private final String key;
    private final String expectedValue;
    private final String actualValue;

    public LicenseInvalidateException(String key, String expectedValue, String actualValue) {
        super(key + ": " + expectedValue + " <> " + actualValue);
        this.key = key;
        this.expectedValue = expectedValue;
        this.actualValue = actualValue;
    }

    public LicenseInvalidateException(String key, String message) {
        super(message);
        this.key = key;
        this.expectedValue = null;
        this.actualValue = null;
    }

    public LicenseInvalidateException(String key, String message, Throwable cause) {
        super(message, cause);
        this.key = key;
        this.expectedValue = null;
        this.actualValue = null;
    }

    public LicenseInvalidateException(String message, Throwable cause) {
        this(null, message, cause);
    }

    public String getKey() {
        return key;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public String getActualValue() {
        return actualValue;
    }
}
