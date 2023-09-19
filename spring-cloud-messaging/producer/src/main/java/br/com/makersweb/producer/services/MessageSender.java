package br.com.makersweb.producer.services;

import br.com.makersweb.producer.domain.Customer;

/**
 * @author aaristides
 */
public interface MessageSender {

    void send(final Customer customer);

}
