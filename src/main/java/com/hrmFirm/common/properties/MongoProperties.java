package com.hrmFirm.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.mongo")
public class MongoProperties {
    private String uri;
    private String database;
}