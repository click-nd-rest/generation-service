package com.github.click.nd.rest.base.service;

import com.github.click.nd.rest.base.domain.Resource;

public interface ResourceService {

    Resource findByName(String name);
    Resource save(Resource resource);
    void deleteByName(String name);
}
