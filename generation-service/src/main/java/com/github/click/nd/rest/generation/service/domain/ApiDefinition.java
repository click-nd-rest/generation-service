package com.github.click.nd.rest.generation.service.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@Value
public class ApiDefinition {

    String id;
    Collection<Resource> resources;
}
