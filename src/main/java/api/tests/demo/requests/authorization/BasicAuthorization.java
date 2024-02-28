package api.tests.demo.requests.authorization;

import java.util.Base64;

import static java.lang.String.format;

public class BasicAuthorization implements Authorization {

    private String username;
    private String password;

    private BasicAuthorization(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static BasicAuthorization withUsernameAndPassword(String username, String password) {
        return new BasicAuthorization(username, password);
    }

    @Override
    public String getAuthorizationHeaderValue() {
        return format("Basic %s", Base64.getEncoder().encodeToString(format("%s:%s", username, password).getBytes()));
    }
}
