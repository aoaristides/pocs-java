package br.com.makersweb.consumer.listener;

import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author aaristides
 */
@Component
@Slf4j
public class MessageReceiver implements MessageConsumer {

    @Override
    @SqsListener(value = "${cloud.aws.queue.name}", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    public void consume(final String input) {
        log.info("Message Receiver: {}", input);
    }
}
