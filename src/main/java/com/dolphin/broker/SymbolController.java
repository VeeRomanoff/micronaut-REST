package com.dolphin.broker;

import com.dolphin.data.InMemoryStorage;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller("api/v1/symbol")
public class SymbolController {

    private final InMemoryStorage inMemoryStorage;

    public SymbolController(InMemoryStorage inMemoryStorage) {
        this.inMemoryStorage = inMemoryStorage;
    }

    @Get
    public List<Symbol> getAllSymbols() {
        return new ArrayList<>(inMemoryStorage.getSymbolsMap().values());
    }

    @Get("/{value}")
    public Symbol getSymbolByValue(@PathVariable String value) {
        return inMemoryStorage.getSymbolsMap().get(value);
    }

    @Get("/filter{?max,offset}")
    public List<Symbol> getSymbols(@QueryValue Optional<Integer> max,
                                   @QueryValue Optional<Integer> offset) {

        return inMemoryStorage.getSymbolsMap().values()
                .stream()
                .skip(offset.orElse(0))
                .limit(max.orElse(10))
                .toList();
    }
}
