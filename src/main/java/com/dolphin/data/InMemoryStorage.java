package com.dolphin.data;

import com.dolphin.broker.Symbol;
import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Singleton
public class InMemoryStorage {

    private Map<String, Symbol> symbolMap = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryStorage.class);
    private final Faker faker = new Faker();

    @PostConstruct
    public void initialize() {
        initializeWith(10);
    }

    public void initializeWith(int numberOfEntries) {
        symbolMap.clear();
        IntStream.range(0, numberOfEntries).forEach(
                i -> addSymbol()
        );
    }

    public void addSymbol() {
        var symbol = new Symbol(faker.stock().nsdqSymbol());
        symbolMap.put(symbol.value(), symbol);
        LOGGER.debug("Added Symbol: {}", symbol);
    }

    public Map<String, Symbol> getSymbolsMap() {
        return symbolMap;
    }
}
