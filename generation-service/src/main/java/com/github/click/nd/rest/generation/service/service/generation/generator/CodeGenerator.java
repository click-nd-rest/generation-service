package com.github.click.nd.rest.generation.service.service.generation.generator;

import com.github.click.nd.rest.generation.service.domain.ApiDefinition;
import java.util.Collection;

public interface CodeGenerator {

    Collection<ResourceSourceCode> generateCode(ApiDefinition apiDefinition);
}
