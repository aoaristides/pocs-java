package br.com.makersweb.consumer.configuration.sqs;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.config.QueueMessageHandlerFactory;
import io.awspring.cloud.messaging.config.SimpleMessageListenerContainerFactory;
import io.awspring.cloud.messaging.listener.QueueMessageHandler;
import io.awspring.cloud.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;

import java.util.Collections;

/**
 * @author aaristides
 */
@Configuration
public class AWSSqsConfiguration {

    private AWSCredentialsProvider credentialsProvider;
    private ObjectMapper mapper;

    public AWSSqsConfiguration(
            @Autowired AWSCredentialsProvider credentialsProvider,
            @Autowired ObjectMapper mapper) {
        this.credentialsProvider = credentialsProvider;
        this.mapper = mapper;
    }

    @Bean
    public AmazonSQSAsync amazonSQSAsync(final AwsClientBuilder.EndpointConfiguration endpointConfiguration) {
        AmazonSQSAsync sqsAsync = AmazonSQSAsyncClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(credentialsProvider)
                .build();

        return new AmazonSQSBufferedAsyncClient(sqsAsync);
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(AmazonSQSAsync amazonSQSAsync) {
        SimpleMessageListenerContainer messageListenerContainer = simpleMessageListenerContainerFactory(amazonSQSAsync).createSimpleMessageListenerContainer();
        messageListenerContainer.setMessageHandler(queueMessageHandler(amazonSQSAsync));
        return messageListenerContainer;
    }

    @Bean
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSQSAsync) {
        SimpleMessageListenerContainerFactory messageListenerContainerFactory = new SimpleMessageListenerContainerFactory();
        messageListenerContainerFactory.setAmazonSqs(amazonSQSAsync);
        return messageListenerContainerFactory;
    }

    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory() {
        QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
        MappingJackson2MessageConverter jacksonMessageConverter = new MappingJackson2MessageConverter();
        jacksonMessageConverter.setSerializedPayloadClass(String.class);
        jacksonMessageConverter.setStrictContentTypeMatch(false);
        jacksonMessageConverter.setObjectMapper(mapper);

        PayloadMethodArgumentResolver payloadArgumentResolver = new PayloadMethodArgumentResolver(jacksonMessageConverter);
        factory.setArgumentResolvers(Collections.singletonList(payloadArgumentResolver));

        return factory;
    }

    @Bean
    public QueueMessageHandler queueMessageHandler(AmazonSQSAsync amazonSQSAsync) {
        queueMessageHandlerFactory().setAmazonSqs(amazonSQSAsync);
        return queueMessageHandlerFactory().createQueueMessageHandler();
    }
}
