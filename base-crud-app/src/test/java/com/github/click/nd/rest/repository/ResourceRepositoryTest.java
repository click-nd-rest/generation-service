package com.github.click.nd.rest.repository;

import com.github.click.nd.rest.base.domain.Resource;
import com.github.click.nd.rest.base.repository.ResourceRepository;
import com.github.click.nd.rest.base.repository.impl.MongoResourceRepositoryImpl;
import com.mongodb.client.MongoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ResourceRepositoryTest.TestConfiguration.class})
public class ResourceRepositoryTest {

    @Autowired
    private ResourceRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void repositoryShouldSuccessfullySaveResource() {
        Resource expected = new Resource("name");
        Resource actualBeforeSaving = repository.findByName(expected.getName()).orElse(null);

        repository.save(expected);

        Resource actualAfterSaving = repository.findByName(expected.getName()).orElseThrow();
        assertNull(actualBeforeSaving);
        assertEquals(expected, actualAfterSaving);
    }

    @Test
    public void repositoryShouldSuccessfullyFindAllResources() {
        Resource first = new Resource("first");
        Resource second = new Resource("second");
        repository.save(first);
        repository.save(second);

        List<Resource> resources = repository.findAll();

        assertEquals(resources.size(), 2);
        assertEquals(resources.get(0), first);
        assertEquals(resources.get(1), second);
    }

    @Test
    public void repositoryShouldSuccessfullyDeleteAllResources() {
        Resource first = new Resource("first");
        Resource second = new Resource("second");
        Resource third = new Resource("third");
        repository.save(first);
        repository.save(second);
        repository.save(third);
        int numberOfResourcesAfterSaving = repository.findAll().size();

        repository.deleteAll();

        int numberOfResourcesAfterDeletion = repository.findAll().size();
        assertEquals(numberOfResourcesAfterSaving, 3);
        assertEquals(numberOfResourcesAfterDeletion, 0);
    }

    @Test
    public void repositoryShouldSuccessfullyDeleteResourceByName() {
        Resource resource = new Resource("name");
        repository.save(resource);
        Resource resourceAfterSaving = repository.findByName(resource.getName()).orElseThrow();

        repository.deleteByName(resource.getName());

        Resource resourceAfterDeletion = repository.findByName(resource.getName()).orElse(null);
        assertEquals(resource, resourceAfterSaving);
        assertNull(resourceAfterDeletion);
    }

    @Configuration
    @EnableMongoTestServer
    @EnableMongoRepositories(basePackageClasses = {ResourceRepository.class})
    protected static class TestConfiguration {

        @Bean
        public ResourceRepository resourceRepository(MongoClient mongoClient) {
            return new MongoResourceRepositoryImpl(mongoClient);
        }
    }
}
