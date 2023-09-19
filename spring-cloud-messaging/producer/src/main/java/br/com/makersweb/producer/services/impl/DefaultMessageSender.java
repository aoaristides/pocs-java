package br.com.makersweb.producer.services.impl;

import br.com.makersweb.producer.configuration.ParameterStoreConfiguration;
import br.com.makersweb.producer.domain.Customer;
import br.com.makersweb.producer.services.MessageSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author aaristides
 */
@Service
public class DefaultMessageSender implements MessageSender {

    private QueueMessagingTemplate queueMessagingTemplate;
    private ParameterStoreConfiguration configuration;
    private ObjectMapper mapper;

    public DefaultMessageSender(
            @Autowired QueueMessagingTemplate queueMessagingTemplate,
            @Autowired ParameterStoreConfiguration configuration,
            @Autowired ObjectMapper mapper
    ) {
        this.queueMessagingTemplate = queueMessagingTemplate;
        this.configuration = configuration;
        this.mapper = mapper;
    }

    @Override
    public void send(final Customer customer) {
        try {
            final var messagePayload = mapper.writeValueAsString(customer);

            final var message = MessageBuilder.withPayload(messagePayload)
                    .setHeader("TransactionId", UUID.randomUUID().toString())
                    .setHeaderIfAbsent("Country", "BR")
                    .build();

            queueMessagingTemplate.convertAndSend(configuration.getQueueName(), message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
