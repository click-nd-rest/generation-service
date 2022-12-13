package com.github.click.nd.rest.generation.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DataType {
    NUMBER("number", int.class),
    STRING("string", String.class);

    /**
     * Should be unique across enum to follow hashcode contract in {@link #persistentHashCode()}
     */
    private final String name;
    private final Class<?> javaClass;

    public String getJavaName() {
        return javaClass.getSimpleName();
    }

    /**
     * Persistent hashcode that rely on unique final fields.
     * Needed to calculate correct persistent hashcode in {@link ResourceField} and later in {@link ApiDefinition}
     */
    public int persistentHashCode() {
        return name.hashCode();
    }
}
