package com.github.click.nd.rest.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.h2.H2Backend;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoTestServerConfiguration {

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }

    @Bean
    public MongoDatabaseFactory mongoDbFactory(MongoServer mongoServer) {
        String connectionString = mongoServer.getConnectionString();
        return new SimpleMongoClientDatabaseFactory(connectionString + "/test");
    }

    @Bean(destroyMethod = "shutdown")
    public MongoServer mongoServer() {
        MongoServer mongoServer = new MongoServer(new H2Backend("database.mv"));
        mongoServer.bind("localhost", 27017);
        return mongoServer;
    }

    @Bean(destroyMethod = "close")
    public MongoClient mongoClient(MongoServer mongoServer) {
        String connectionString = mongoServer.getConnectionString();
        return MongoClients.create(connectionString);
    }
}
