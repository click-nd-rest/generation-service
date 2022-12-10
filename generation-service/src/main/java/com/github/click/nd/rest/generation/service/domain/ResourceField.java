package com.github.click.nd.rest.generation.service.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.click.nd.rest.generation.service.util.CaseUtil;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class ResourceField {

    String name;
    DataType type;

    /**
     * Methods used in .mustache templates
     */
    public String getNameUpperCamel() {
        return CaseUtil.toUpperCamel(name);
    }

    public String getNameLowerCamel() {
        return CaseUtil.toLowerCamel(name);
    }
}
