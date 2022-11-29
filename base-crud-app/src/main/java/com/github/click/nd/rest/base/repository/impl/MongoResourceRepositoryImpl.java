package com.github.click.nd.rest.base.repository.impl;

import com.github.click.nd.rest.base.domain.Resource;
import com.github.click.nd.rest.base.repository.ResourceRepository;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MongoResourceRepositoryImpl implements ResourceRepository {

    private final MongoClient client;

    @Override
    public Optional<Resource> findByName(String name) {
        Bson filter = new Document("name", name);
        Document document = getCollection().find(filter).first();
        return (document == null)
                ? Optional.empty()
                : Optional.of(new Resource((String) document.get("name")));
    }

    @Override
    public List<Resource> findAll() {
        FindIterable<Document> documents = getCollection().find();
        List<Resource> resources = new ArrayList<>();
        documents.forEach(document -> resources.add(new Resource((String) document.get("name"))));
        return resources;
    }

    @Override
    public Resource save(Resource resource) {
        Document document = new Document("name", resource.getName());
        getCollection().insertOne(document);
        return resource;
    }

    @Override
    public void deleteByName(String name) {
        Bson filter = new Document("name", name);
        getCollection().deleteOne(filter);
    }

    @Override
    public void deleteAll() {
        getCollection().drop();
    }

    private MongoCollection<Document> getCollection() {
        return client.getDatabase("testdb").getCollection("resources");
    }
}
