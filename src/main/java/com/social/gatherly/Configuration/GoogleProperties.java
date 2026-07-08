package com.social.gatherly.Configuration;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "google")
public class GoogleProperties {
    private String clientId;

    private String clientSecret;

    private String redirectUri;

    private String tokenUri;

    private String userInfoUri;



}
