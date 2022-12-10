package com.github.click.nd.rest.generation.service.controller;

import com.github.click.nd.rest.generation.service.domain.ApiDefinition;
import com.github.click.nd.rest.generation.service.domain.GenerateApiResponse;
import com.github.click.nd.rest.generation.service.service.generation.ApiDefinitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiDefinitionController {

    private final ApiDefinitionService apiDefinitionService;

    @PostMapping("/v1/api-definition")
    public GenerateApiResponse generateApi(@RequestBody ApiDefinition apiDefinition) {
        return apiDefinitionService.generateCodeIfDefinitionNotPushed(apiDefinition);
    }
}
