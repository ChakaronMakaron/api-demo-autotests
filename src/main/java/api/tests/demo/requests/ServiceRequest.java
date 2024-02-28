package api.tests.demo.requests;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import api.tests.demo.listeners.RequestListener;
import api.tests.demo.requests.authorization.Authorization;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RedirectConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ServiceRequest {

    private final RequestSpecification requestSpecification;

    public ServiceRequest(String baseUri, String endpoint) {
        this.requestSpecification = buildRequestSpecification(baseUri, endpoint);
    }

    public ServiceRequest(String baseUri) {
        this(baseUri, StringUtils.EMPTY);
    }

    @Step("GET")
    public Response GET() {
        return prepareRequestSpecification()
            .get();
    }

    @Step("POST")
    public Response POST() {
        return prepareRequestSpecification()
                .post();
    }

    @Step("PUT")
    public Response PUT() {
        return prepareRequestSpecification()
                .put();
    }

    @Step("PATCH")
    public Response PATCH() {
        return prepareRequestSpecification()
                .patch();
    }

    @Step("DELETE")
    public Response DELETE() {
        return prepareRequestSpecification()
                .delete();
    }

    public ServiceRequest body(RequestPayload payload) {
        requestSpecification.body(payload);
        return this;
    }

    public ServiceRequest body(Map<String, Object> payload) {
        requestSpecification.body(payload);
        return this;
    }

    public ServiceRequest headers(Map<String, String> headers) {
        requestSpecification.headers(headers);
        return this;
    }

    public ServiceRequest cookies(Map<String, String> cookies) {
        requestSpecification.cookies(cookies);
        return this;
    }

    public ServiceRequest pathParams(Map<String, String> pathParams) {
        requestSpecification.pathParams(pathParams);
        return this;
    }

    public ServiceRequest queryParams(Map<String, String> queryParams) {
        requestSpecification.queryParams(queryParams);
        return this;
    }

    public ServiceRequest doNotLog() {
        requestSpecification.noFilters();
        return this;
    }

    public ServiceRequest authorization(Authorization authorization) {
        requestSpecification.header(AUTHORIZATION,
        authorization.getAuthorizationHeaderValue());
        return this;
    }

    private RequestSpecification prepareRequestSpecification() {
        return RestAssured.given()
                .spec(this.requestSpecification);
    }

    private RequestSpecification buildRequestSpecification(
        String baseUri,
        String endpoint
    ) {
        return new RequestSpecBuilder()
                .setUrlEncodingEnabled(true)
                .addFilter(RequestListener.getInstance())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setBaseUri(baseUri)
                .setBasePath(endpoint)
                .setConfig(RestAssured.config()
                        .redirect(RedirectConfig.redirectConfig().followRedirects(true))
                        .sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation())
                        .httpClient(HttpClientConfig.httpClientConfig()
                                .setParam("http.socket.timeout",
                                        30000)
                                .setParam("http.connection.timeout",
                                        10000)))
                .build();
    }
}
