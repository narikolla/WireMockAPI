import com.github.tomakehurst.wiremock.WireMockServer;
import utils.JsonUtil;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class Stubs {
    private JsonUtil jsonUtil;
    public WireMockServer wireMockServer;

    public Stubs setUp() {
        wireMockServer = new WireMockServer(3467);
        wireMockServer.start();
        jsonUtil = new JsonUtil();
        return this;
    }

    public Stubs resetServer() {
        wireMockServer.resetAll();
        return this;
    }

    public Stubs stubForCreateCart(String responseFileName) {
        wireMockServer.stubFor(post("/carts")
            .withHeader("Content-Type", equalToIgnoreCase("application/json"))
            .withHeader("Authorization", equalToIgnoreCase("Bearer WireMockToken"))
            .withRequestBody(matchingJsonPath("$.customer.firstName", equalTo("Narendra")))
            .withRequestBody(matchingJsonPath("$.customer.lastName", equalTo("Kolla")))
            .willReturn(aResponse()
                .withStatus(201)
                .withHeader("Content-Type", "application/json")
                .withBodyFile("json/" + responseFileName)));
        return this;
    }

    public Stubs stubForCreateCartError(String responseFileName) {
        wireMockServer.stubFor(post("/carts")
            .withHeader("Authorization", equalToIgnoreCase("Bearer Error"))
            .willReturn(aResponse()
                .withStatus(401)
                .withHeader("Content-Type", "application/json")
                .withBodyFile("json/" + responseFileName)));
        return this;
    }

    public Stubs stubForGetCartSingle(String responseFileName) {
            wireMockServer.stubFor(get("/carts/1?productCount=1")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBodyFile("json/" + responseFileName)));
        return this;
    }

    public Stubs stubForGetCartDouble(String responseFileName) {
        wireMockServer.stubFor(get("/carts/1?productCount=2")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBodyFile("json/" + responseFileName)));
        return this;
    }

    public Stubs stubForAddProduct(String requestFileName, String responseFileName) {
        jsonUtil.setJSON("/__files/json/".concat(requestFileName));
        wireMockServer.stubFor(post("/carts/1")
            .withRequestBody(equalToJson(jsonUtil.getJSON().toString(), true, true))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBodyFile("json/" + responseFileName)));
        return this;
    }

    public Stubs stubForPutProduct(String requestFileName, String responseFileName) {
        jsonUtil.setJSON("/__files/json/".concat(requestFileName));
        wireMockServer.stubFor(post("/carts/1/products/1")
            .withRequestBody(equalToJson(jsonUtil.getJSON().toString(), true, true))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBodyFile("json/" + responseFileName)));
        return this;
    }

    public Stubs stubForDeleteProduct() {
        wireMockServer.stubFor(delete("/carts/1/products/1")
            .willReturn(aResponse()
                .withStatus(204)));
        return this;
    }

    public Stubs stubForDeleteCart() {
        wireMockServer.stubFor(delete("/carts/klms2f4c-8129-4a4b-b32d-550b7fc3cfb2")
            .willReturn(aResponse()
                .withStatus(204)));
        return this;
    }

    public Stubs status() {
        System.out.println("Stubs Started!");
        return this;
    }
}
