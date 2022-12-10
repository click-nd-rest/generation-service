package com.github.click.nd.rest.generation.service.service.generation;

import com.github.click.nd.rest.generation.service.domain.ApiDefinition;
import com.github.click.nd.rest.generation.service.domain.DataType;
import com.github.click.nd.rest.generation.service.domain.Resource;
import com.github.click.nd.rest.generation.service.domain.ResourceField;
import com.github.click.nd.rest.generation.service.service.generation.generator.CodeGeneratorImpl;
import com.github.click.nd.rest.generation.service.service.generation.generator.GenerationResource;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Setting same parameter value for explicitness
 */
@SuppressWarnings("SameParameterValue")
@SpringBootTest
public abstract class BaseCodeGeneratorTest {
    @Autowired
    protected CodeGeneratorImpl codeGenerator;

    protected GenerationResource resource(String name, Collection<ResourceField> resourceFields) {
        return GenerationResource.of(new Resource(name, resourceFields));
    }

    protected ResourceField resourceField(String name, DataType type) {
        return new ResourceField(name, type);
    }

    protected ApiDefinition apiDefinition(String id, Collection<Resource> resources) {
        return new ApiDefinition(id, resources);
    }

    protected Resource rawResource(String name, Collection<ResourceField> resourceFields) {
        return new Resource(name, resourceFields);
    }
}
