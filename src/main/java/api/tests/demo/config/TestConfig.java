package api.tests.demo.config;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TestConfig {

    private String evenOrOddServiceHost;
    private String nationalityByNameServiceHost;
    private String cocktailRecipesServiceHost;
}
