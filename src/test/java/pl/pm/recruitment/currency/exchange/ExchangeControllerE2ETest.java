package pl.pm.recruitment.currency.exchange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExchangeControllerE2ETest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;



    @Test
    void createExchange_shouldReturnOk() {
        ClientDto client = new ClientDto();
        client.setFirstName("John");
        client.setLastName("Smith");
        client.setBalance(new BigDecimal("100.00"));

        String accountNumber = clientService.save(client);

        ExchangeDto exchangeDto = new ExchangeDto();
        exchangeDto.setAmountToExchange(BigDecimal.TEN);
        exchangeDto.setCurrencyFrom(Currency.PLN);
        exchangeDto.setCurrencyTo(Currency.USD);
        exchangeDto.setBankAccount(accountNumber);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/exchanges",
                HttpMethod.POST,
                new HttpEntity<>(exchangeDto),
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
