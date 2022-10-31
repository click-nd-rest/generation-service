package com.github.click.nd.rest.generation.service.controller;

import com.github.click.nd.rest.generation.service.domain.ApiDefinition;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class ApiDefinitionController {
    @PostMapping("/v1/api-definition")
    public void generateApi(@RequestBody ApiDefinition apiDefinition) {
    }
}
