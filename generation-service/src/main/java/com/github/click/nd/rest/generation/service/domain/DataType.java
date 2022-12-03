package com.github.click.nd.rest.generation.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DataType {
    NUMBER("number", int.class),
    STRING("string", String.class);

    private final String name;
    private final Class<?> javaClass;

    public String getJavaName() {
        return javaClass.getSimpleName();
    }
}
