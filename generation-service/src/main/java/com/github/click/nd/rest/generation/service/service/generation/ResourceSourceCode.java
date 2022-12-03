package com.github.click.nd.rest.generation.service.service.generation;


import com.github.click.nd.rest.generation.service.util.CaseUtil;

public record ResourceSourceCode(String resourceName, String entityCode, String repositoryCode, String controllerCode) {
    public String getResourceNameUpperCamel() {
        return CaseUtil.toUpperCamel(resourceName);
    }
}
