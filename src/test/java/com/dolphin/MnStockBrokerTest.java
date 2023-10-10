package com.dolphin;

import com.dolphin.broker.Symbol;
import com.dolphin.data.InMemoryStorage;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class SymbolsControllerTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(SymbolsControllerTest.class);

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Inject
    InMemoryStorage inMemoryStorage;

    @BeforeEach
    void setUp() {
        inMemoryStorage.initializeWith(10);
    }

    @Test
    void symbolsEndpointReturnsListOfSymbols() {
        var response = httpClient.toBlocking().exchange("api/v1/symbol", JsonNode.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(10, response.getBody().get().size());
    }

    @Test
    void symbolEndpointReturnsCorrectSymbol() {
        var testSymbol = new Symbol("TEST");
        inMemoryStorage.getSymbolsMap().put(testSymbol.value(), testSymbol);

        var response = httpClient.toBlocking().exchange("api/v1/symbol" + "/" + testSymbol.value(), Symbol.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(testSymbol, response.getBody().get());
    }

    @Test
    void symbolsEndpointReturnsListOfSymbolsTakingMaxQueryParametersIntoAccount() {
        var max10 = httpClient.toBlocking().exchange("api/v1/symbol/filter?max=10", JsonNode.class);
        assertEquals(HttpStatus.OK, max10.getStatus());
        LOGGER.debug("maximum 10: {}", max10.getBody().get().toString());
        assertEquals(10, max10.getBody().get().size());
    }

    @Test
    void symbolsEndpointReturnsListOfSymbolsTakingOffsetQueryParametersIntoAccount() {
        var offset7 = httpClient.toBlocking().exchange("api/v1/symbol/filter?offset=7", JsonNode.class);
        assertEquals(HttpStatus.OK, offset7.getStatus());
        LOGGER.debug("offset 10: {}", offset7.getBody().get().toString());
        assertEquals(3, offset7.getBody().get().size());
    }

    @Test
    void symbolsEndpointReturnsListOfSymbolsTakingOffsetAndMaxQueryParametersIntoAccount() {
        var max30Offset7 = httpClient.toBlocking().exchange("api/v1/symbol/filter?max=3&offset=7", JsonNode.class);
        assertEquals(HttpStatus.OK, max30Offset7.getStatus());
        LOGGER.debug("max 2, offset 7: {}", max30Offset7.getBody().get().toString());
        assertEquals(3, max30Offset7.getBody().get().size());
    }

    @Test
    void symbolsEndpointReturnsListOfSymbolsReturnsListOfSymbolsIfProvidingOnlyFilter() {
        var res = httpClient.toBlocking().exchange("api/v1/symbol/filter", JsonNode.class);
        assertEquals(HttpStatus.OK, res.getStatus());
        assertEquals(10, res.getBody().get().size());
    }
}
