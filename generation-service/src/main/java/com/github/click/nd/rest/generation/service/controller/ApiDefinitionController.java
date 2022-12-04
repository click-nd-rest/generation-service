package com.github.click.nd.rest.generation.service.controller;

import com.github.click.nd.rest.generation.service.domain.ApiDefinition;
import com.github.click.nd.rest.generation.service.domain.GenerateApiResponse;
import com.github.click.nd.rest.generation.service.service.generation.ApiDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class ApiDefinitionController {
    private final ApiDefinitionService apiDefinitionService;

    @Autowired
    public ApiDefinitionController(ApiDefinitionService apiDefinitionService) {
        this.apiDefinitionService = apiDefinitionService;
    }

    @PostMapping("/v1/api-definition")
    public GenerateApiResponse generateApi(@RequestBody ApiDefinition apiDefinition) {
        return apiDefinitionService.generateCodeIfDefinitionNotPushed(apiDefinition);
    }
}
