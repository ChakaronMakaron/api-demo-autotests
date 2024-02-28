package api.tests.demo.config.environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.String.format;

public class EnvironmentContainer {

    private static final Map<String, String> ENV = new LinkedHashMap<>();

    public static synchronized void add(String key, String value) {
        ENV.put(key, String.valueOf(value));
    }

    public static synchronized void writeEnvironment(String allureEnvFilePath) {
        String absPath = new File(allureEnvFilePath).getAbsolutePath();
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(absPath, true))) {
            ENV.forEach((k, v) -> printWriter.println(format("%s=%s", k, v)));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
