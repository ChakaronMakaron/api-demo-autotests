package api.tests.demo.config;

import static api.tests.demo.config.environment.EnvironmentKey.COCKTAIL_RECIPES_SERVICE_HOST;
import static api.tests.demo.config.environment.EnvironmentKey.EVEN_OR_ODD_SERVICE_HOST;
import static api.tests.demo.config.environment.EnvironmentKey.NATIONALITY_BY_NAME_SERVICE_HOST;
import static api.tests.demo.utils.EnvironmentHelper.getEnv;
import static api.tests.demo.utils.EnvironmentHelper.getEnvPropertyOrThrow;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

import api.tests.demo.config.environment.EnvConstants;
import api.tests.demo.config.environment.EnvironmentContainer;
import api.tests.demo.config.environment.EnvironmentKey;
import api.tests.demo.constants.ProjectConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigContainer {

    private static TestConfig config;

    public static synchronized TestConfig getConfig() {
        if (Objects.isNull(config)) {
            config = loadConfig();
        }
        return config;
    }

    private static TestConfig loadConfig() {
        if (localConfig()) {
            loadLocalConfig();
        }

        verifyEnv();

        TestConfig testConfig = TestConfig.builder()
                .evenOrOddServiceHost(getEnv(EVEN_OR_ODD_SERVICE_HOST.name()))
                .nationalityByNameServiceHost(getEnv(NATIONALITY_BY_NAME_SERVICE_HOST.name()))
                .cocktailRecipesServiceHost(getEnv(COCKTAIL_RECIPES_SERVICE_HOST.name()))
                .build();

        Arrays.stream(EnvironmentKey.values())
                .forEach(ConfigContainer::addToEnvContainer);

        return testConfig;
    }

    private static void verifyEnv() {
        StringBuilder message = new StringBuilder();
        log.info("Environment verification");
        Arrays.stream(EnvironmentKey.values()).forEach(key -> {
            try {
                String val = getEnvPropertyOrThrow(key.name());
                log.info("{} = {}", key.name(), key.isSecret() ? ProjectConstants.SECRET : val);
            } catch (IllegalStateException e) {
                message.append(e.getMessage()).append("\n");
            }
        });
        if (message.length() != 0) {
            log.info(message.toString());
            throw new IllegalStateException(message.toString());
        }
    }

    private static void loadLocalConfig() {
        log.info("Loading local config");
        try {
            Properties localProperties = new Properties();
            localProperties.load(new FileReader("local.properties"));
            localProperties.forEach((k, v) -> {
                System.setProperty(String.valueOf(k), String.valueOf(v));
            });
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void addToEnvContainer(EnvironmentKey key) {
        EnvironmentContainer.add(key.name(), key.isSecret()
                ? ProjectConstants.SECRET
                : getEnv(key.name()));
    }

    private static boolean localConfig() {
        return EnvConstants.TEST_CONFIG_LOCAL
                .equals(getEnv(EnvConstants.TEST_CONFIG_ENV));
    }
}
