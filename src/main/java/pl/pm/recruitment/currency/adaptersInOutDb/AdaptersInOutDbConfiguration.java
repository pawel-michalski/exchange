package pl.pm.recruitment.currency.adaptersInOutDb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.pm.recruitment.currency.exchange.AccountRepository;
import pl.pm.recruitment.currency.exchange.ClientRepository;
import pl.pm.recruitment.currency.exchange.ExchangeRepository;

@Configuration
public class AdaptersInOutDbConfiguration {

    @Bean
    AccountRepository accountRepository(AccountJpaRepository accountJpaRepository) {
        return new AccountRepositoryAdapter(accountJpaRepository);
    }

    @Bean
    ClientRepository clientRepository(ClientJpaRepository clientJpaRepository) {
        return new ClientRepositoryAdapter(clientJpaRepository);
    }

    @Bean
    ExchangeRepository exchangeRepository(ExchangeJpaRepository exchangeJpaRepository) {
        return new ExchangeRepositoryAdapter(exchangeJpaRepository);
    }
}
