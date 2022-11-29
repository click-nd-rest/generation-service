package com.github.click.nd.rest.base.controller;

import com.github.click.nd.rest.base.domain.Resource;
import com.github.click.nd.rest.base.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService service;

    @GetMapping(path = "/{name}")
    @ResponseStatus(HttpStatus.OK)
    public Resource findByName(@PathVariable String name) {
        return service.findByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Resource create(@RequestBody Resource resource) {
        return service.save(resource);
    }

    @DeleteMapping(path = "/{name}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteByName(@PathVariable String name) {
        service.deleteByName(name);
    }
}
