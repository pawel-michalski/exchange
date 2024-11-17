package pl.pm.recruitment.currency.adaptersInOutDb;

import pl.pm.recruitment.currency.exchange.Exchange;
import pl.pm.recruitment.currency.exchange.ExchangeRepository;

import java.util.Optional;

public class ExchangeRepositoryAdapter implements ExchangeRepository {

    private final ExchangeJpaRepository exchangeJpaRepository;

    public ExchangeRepositoryAdapter(ExchangeJpaRepository exchangeJpaRepository) {
        this.exchangeJpaRepository = exchangeJpaRepository;
    }

    @Override
    public Optional<Exchange> findById(Long id) {
        return exchangeJpaRepository.findById(id);
    }

    @Override
    public void save(Exchange exchange) {
        exchangeJpaRepository.save(exchange);
    }
}
