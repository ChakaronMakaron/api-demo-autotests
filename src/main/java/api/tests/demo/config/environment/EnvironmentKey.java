package api.tests.demo.config.environment;

public enum EnvironmentKey {

    EVEN_OR_ODD_SERVICE_HOST(false),
    NATIONALITY_BY_NAME_SERVICE_HOST(false),
    COCKTAIL_RECIPES_SERVICE_HOST(false);

    private final boolean isSecret;

    EnvironmentKey(boolean isSecret) {
        this.isSecret = isSecret;
    }

    public boolean isSecret() {
        return isSecret;
    }
}
