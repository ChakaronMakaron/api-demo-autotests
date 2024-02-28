package api.tests.demo.requests.authorization;

import static java.lang.String.format;

public class BearerAuthorization implements Authorization {

    private final String token;

    private BearerAuthorization(String token) {
        this.token = token;
    }

    public static BearerAuthorization withToken(String token) {
        return new BearerAuthorization(token);
    }

    @Override
    public String getAuthorizationHeaderValue() {
        return format("Bearer %s", token);
    }
}
