package com.github.click.nd.rest.base.repository;

import com.github.click.nd.rest.base.domain.Resource;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository {

    Optional<Resource> findByName(String name);
    List<Resource> findAll();
    Resource save(Resource resource);
    void deleteByName(String name);
    void deleteAll();
}
