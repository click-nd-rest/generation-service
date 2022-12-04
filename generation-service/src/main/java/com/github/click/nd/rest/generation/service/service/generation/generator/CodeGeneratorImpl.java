package com.github.click.nd.rest.generation.service.service.generation.generator;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

import com.github.click.nd.rest.generation.service.domain.ApiDefinition;
import com.github.click.nd.rest.generation.service.domain.Resource;
import com.github.mustachejava.MustacheFactory;
import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeGeneratorImpl implements CodeGenerator {
    private MustacheFactory mustacheFactory;

    @SneakyThrows
    @Override
    public Collection<ResourceSourceCode> generateCode(ApiDefinition apiDefinition) {
        var resources = apiDefinition.getResources();
        var sourceCodes = new ArrayList<ResourceSourceCode>(apiDefinition.getResources().size());
        resources.forEach(r -> sourceCodes.add(createResourceSourceCode(r)));
        return sourceCodes;
    }

    private ResourceSourceCode createResourceSourceCode(Resource rawResource) {
        var resource = GenerationResource.of(rawResource);
        var entityCode = generateEntityCode(resource);
        var repositoryCode = generateRepositoryCode(resource);
        var controllerCode = generateControllerCode(resource);
        return new ResourceSourceCode(resource.getName(), entityCode, repositoryCode, controllerCode);
    }

    @SneakyThrows
    @VisibleForTesting
    String generateEntityCode(GenerationResource resource) {
        return generateResourceCodeFromTemplate(resource, "mustache/templates/Resource.mustache");
    }

    @SneakyThrows
    @VisibleForTesting
    String generateRepositoryCode(GenerationResource resource) {
        return generateResourceCodeFromTemplate(resource, "mustache/templates/Repository.mustache");
    }

    @SneakyThrows
    @VisibleForTesting
    String generateControllerCode(GenerationResource resource) {
        return generateResourceCodeFromTemplate(resource, "mustache/templates/Controller.mustache");
    }

    private String generateResourceCodeFromTemplate(GenerationResource resource, String templatePath) throws IOException {
        var mustache = mustacheFactory.compile(templatePath);
        var stringWriter = new StringWriter();
        mustache.execute(stringWriter, resource).flush();
        return stringWriter.toString();
    }
}
