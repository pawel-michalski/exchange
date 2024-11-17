package pl.pm.recruitment.currency.exchange;


import java.util.Optional;

/** It is a SPI (Service Provider interface), a Port, for an active side,
 * to hide a db, make an abstraction of it. */

public interface ExchangeRepository {

    Optional<Exchange> findById(Long id);

    void save(Exchange exchange);
}