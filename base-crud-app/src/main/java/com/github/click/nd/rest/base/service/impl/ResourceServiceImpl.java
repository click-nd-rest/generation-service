package com.github.click.nd.rest.base.service.impl;

import com.github.click.nd.rest.base.domain.Resource;
import com.github.click.nd.rest.base.exception.impl.ResourceNotFoundException;
import com.github.click.nd.rest.base.repository.ResourceRepository;
import com.github.click.nd.rest.base.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository repository;

    @Override
    public Resource findByName(String name) {
        return repository.findByName(name).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Resource save(Resource resource) {
        return repository.save(resource);
    }

    @Override
    public void deleteByName(String name) {
        repository.deleteByName(name);
    }
}
