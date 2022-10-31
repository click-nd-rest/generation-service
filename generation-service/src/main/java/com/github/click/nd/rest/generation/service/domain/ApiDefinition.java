package com.github.click.nd.rest.generation.service.domain;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class ApiDefinition {
    String id;
    Collection<Resource> resourceFields;
}
