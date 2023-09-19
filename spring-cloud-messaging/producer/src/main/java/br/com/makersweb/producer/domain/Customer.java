package br.com.makersweb.producer.domain;


/**
 * @author aaristides
 * @param name
 * @param mail
 * @param phone
 */
public record Customer(
        String name,
        String mail,
        String phone
) {
}
