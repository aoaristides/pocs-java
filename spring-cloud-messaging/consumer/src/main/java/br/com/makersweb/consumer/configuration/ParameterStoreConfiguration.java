package br.com.makersweb.consumer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author aaristides
 */
@Configuration
@EnableConfigurationProperties
public class ParameterStoreConfiguration {

    @Value("${cloud.aws.queue.endpoint}")
    private String endpoint;


    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.profile.name}")
    private String profile;

    public String getEndpoint() {
        return endpoint;
    }

    public String getRegion() {
        return region;
    }

    public String getProfile() {
        return profile;
    }
}
