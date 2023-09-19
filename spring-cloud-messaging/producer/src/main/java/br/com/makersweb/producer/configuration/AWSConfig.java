package br.com.makersweb.producer.configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author aaristides
 */
@Configuration
public class AWSConfig {

    private ParameterStoreConfiguration configuration;

    public AWSConfig(@Autowired ParameterStoreConfiguration configuration) {
        this.configuration = configuration;
    }

    @Bean
    @Primary
    public AWSCredentialsProvider credentials() {
        return new ProfileCredentialsProvider(configuration.getProfile());
    }

    @Bean
    public AwsClientBuilder.EndpointConfiguration endpointConfiguration() {
        return new AwsClientBuilder.EndpointConfiguration(configuration.getEndpoint(), configuration.getRegion());
    }

}
