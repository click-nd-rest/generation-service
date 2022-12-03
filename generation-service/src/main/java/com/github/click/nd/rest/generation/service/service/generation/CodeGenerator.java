package com.github.click.nd.rest.generation.service.service.generation;

import java.util.Collection;

import com.github.click.nd.rest.generation.service.domain.ApiDefinition;

public interface CodeGenerator {
    Collection<ResourceSourceCode> generateCode(ApiDefinition apiDefinition);
}
