package com.github.click.nd.rest.generation.service.service.generation.generator;

import java.util.Collection;

import com.github.click.nd.rest.generation.service.domain.DataType;
import com.github.click.nd.rest.generation.service.domain.Resource;
import com.github.click.nd.rest.generation.service.domain.ResourceField;
import com.github.click.nd.rest.generation.service.util.CaseUtil;
import com.google.common.collect.ImmutableList;
import lombok.Value;

@Value
public class GenerationResource {
    String name;
    ResourceField idField;
    Collection<ResourceField> resourceFields;

    private GenerationResource(String name, ResourceField idField, Collection<ResourceField> resourceFields) {
        this.name = name;
        this.idField = idField;
        this.resourceFields = resourceFields;
    }

    public String getResourceNameUpperCamel() {
        return CaseUtil.toUpperCamel(name);
    }

    public String getResourceNameLowerCamel() {
        return CaseUtil.toLowerCamel(name);
    }

    public String getResourceNameLowerHyphen() {
        return CaseUtil.toLowerHyphen(name);
    }

    public static GenerationResource of(Resource resource) {
        var resourceFields = resource.getResourceFields();
        var idField = resourceFields.stream()
            .filter(f -> f.getName().equalsIgnoreCase("id"))
            .findFirst()
            .orElse(new ResourceField("id", DataType.STRING));

        var generationResourceFields = new ImmutableList.Builder<ResourceField>()
            .add(idField)
            .addAll(resourceFields)
            .build();

        return new GenerationResource(
            resource.getName(),
            idField,
            generationResourceFields
        );
    }
}
