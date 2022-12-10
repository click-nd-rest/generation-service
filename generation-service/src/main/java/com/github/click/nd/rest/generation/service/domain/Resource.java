package com.github.click.nd.rest.generation.service.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class Resource {

    String name;
    Collection<ResourceField> resourceFields;
}
