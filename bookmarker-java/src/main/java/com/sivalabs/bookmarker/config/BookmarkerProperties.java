package com.sivalabs.bookmarker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import static com.sivalabs.bookmarker.domain.utils.Constants.DEFAULT_JWT_TOKEN_EXPIRES;

@ConfigurationProperties(prefix = "bookmarker")
@Data
public class BookmarkerProperties {

    private JwtConfig jwt = new JwtConfig();

    @Data
    public static class JwtConfig {
        private String issuer = "bookmarker";
        private String header = "Authorization";
        private Long expiresIn = DEFAULT_JWT_TOKEN_EXPIRES;
        private String secret = "";
    }
}
