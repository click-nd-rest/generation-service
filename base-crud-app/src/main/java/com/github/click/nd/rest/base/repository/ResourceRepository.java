package com.github.click.nd.rest.base.repository;

import com.github.click.nd.rest.base.domain.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ResourceRepository extends MongoRepository<Resource, String> {
    List<Resource> findResourcesByName(String name);
    void deleteByName(String name);
}
