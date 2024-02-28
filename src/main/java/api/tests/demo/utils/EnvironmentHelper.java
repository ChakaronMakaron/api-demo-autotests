package api.tests.demo.utils;

import java.util.Objects;

public class EnvironmentHelper {

    public static String getEnvPropertyOrThrow(String property) {
        String processProp = System.getProperty(property);
        if (Objects.nonNull(processProp)) return processProp;
        String sysProp = System.getenv(property);
        if (Objects.nonNull(sysProp)) return sysProp;
        throw new IllegalStateException("Environment property '%s' is not specified".formatted(property));
    }

    public static String getEnv(String property) {
        String processProp = System.getProperty(property);
        if (Objects.nonNull(processProp)) return processProp;
        String sysProp = System.getenv(property);
        if (Objects.nonNull(sysProp)) return sysProp;
        return null;
    }
}
