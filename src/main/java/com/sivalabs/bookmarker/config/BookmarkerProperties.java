package com.sivalabs.bookmarker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bookmarker")
@Data
public class BookmarkerProperties {
    private boolean importDataEnabled = true;
    private String importFilePath;
    private String adminEmail;
    private String logstashHost;
}
