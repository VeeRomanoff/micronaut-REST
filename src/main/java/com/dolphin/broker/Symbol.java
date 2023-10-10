package com.dolphin.broker;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record Symbol(String value) {
}
