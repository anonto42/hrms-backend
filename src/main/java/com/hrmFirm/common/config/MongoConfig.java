package com.hrmFirm.common.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.hrmFirm.common.properties.MongoProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Slf4j
@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com")
@EnableConfigurationProperties(MongoProperties.class)
@AllArgsConstructor
public class MongoConfig {

    private final MongoProperties properties;

    @Bean
    public MongoClient mongoClient() {
        log.info("Initializing MongoDB client");

        ConnectionString connectionString = new ConnectionString(properties.getUri());

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .retryWrites(true)
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        log.info("Creating MongoTemplate for database: {}", properties.getDatabase());
        return new MongoTemplate(mongoClient, properties.getDatabase());
    }
}