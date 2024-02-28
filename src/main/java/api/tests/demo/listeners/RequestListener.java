package api.tests.demo.listeners;


import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.Cookies;
import io.restassured.internal.support.Prettifier;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class RequestListener implements Filter {

    private static final RequestListener instance = new RequestListener();

    public static RequestListener getInstance() {
        return instance;
    }

    private final String EMPTY = "Empty";
    private final Prettifier prettifier = new Prettifier();

    @Override
    public Response filter(
            FilterableRequestSpecification requestSpec,
            FilterableResponseSpecification responseSpec,
            FilterContext ctx
    ) {
        log.info("----- HTTP Request -----");
        log.info("{} {}", requestSpec.getMethod(), requestSpec.getURI());
        log.info("Headers:\n{}", requestSpec.getHeaders());
        log.info("Cookies:\n{}", cookiesToString(requestSpec.getCookies()));
        log.info("Body:\n{}", prettifier.prettify(requestBodyToString(requestSpec), Parser.JSON));

        Response response = ctx.next(requestSpec, responseSpec);

        log.info("----- HTTP Response -----");
        log.info("Status: {}", response.getStatusLine());
        log.info("Headers:\n{}", response.getHeaders());
        log.info("Cookies:\n{}", cookiesToString(response.getDetailedCookies()));
        log.info("Response Body:\n{}", responseBodyAsString(response));

        return response;
    }

    private String cookiesToString(Cookies cookies) {
        return cookies.size() == 0
                ? EMPTY
                : cookies.toString();
    }

    private String requestBodyToString(FilterableRequestSpecification requestSpecification) {
        return Objects.nonNull(requestSpecification.getBody())
                ? requestSpecification.getBody()
                : EMPTY;
    }

    private String responseBodyAsString(Response response) {
        return response.getBody().asByteArray().length != 0
                ? response.getBody().asPrettyString()
                : EMPTY;
    }
}
