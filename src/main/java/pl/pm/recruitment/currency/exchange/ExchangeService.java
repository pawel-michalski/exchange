package pl.pm.recruitment.currency.exchange;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Service
@Slf4j
public class ExchangeService {

    private final AccountService accountService;
    private final ExchangeRateService exchangeRateService;
    private final ExchangeRepository exchangeRepository;


    public ExchangeService(AccountService accountService, ExchangeRateService exchangeRateService, ExchangeRepository exchangeRepository) {
        this.accountService = accountService;
        this.exchangeRateService = exchangeRateService;
        this.exchangeRepository = exchangeRepository;
    }

    @Transactional
    public void exchange(ExchangeDto exchangeDto) {

        if (exchangeDto.getCurrencyTo().equals(exchangeDto.getCurrencyFrom())) {
            String errorMsg = "Currency has to not be same type";
            log.error(errorMsg);
            throw new BusinessException(errorMsg);
        }
        Set<Account> userAccounts = accountService.loadUserAccounts(exchangeDto);
        Client user = accountService.loadUser(userAccounts);

        Account accountFrom = accountService.getAccountFrom(userAccounts, exchangeDto.getCurrencyFrom());
        Account accountTo = accountService.getAccountTo(user, userAccounts, exchangeDto.getCurrencyTo());

        ExchangeRateJsonResponse exchangeRate = exchangeRateService.getExchangeRate(exchangeDto.getCurrencyTo());

        BigDecimal rate = exchangeRate.getRates().stream()
                .map(ExchangeRateJsonResponse.Rate::getMid)
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Brak dostępnego kursu wymiany dla waluty docelowej!, {}.", user);
                    return new IllegalStateException("Brak dostępnego kursu wymiany dla waluty docelowej!, " + user);
                });

        BigDecimal amountToTransfer = exchangeDto.getAmountToExchange().multiply(rate);

        accountService.loadAccounts(exchangeDto, rate, accountFrom, accountTo);

        persistEvent(accountFrom, accountTo, exchangeDto.getAmountToExchange(), amountToTransfer);

    }



    private void persistEvent(Account debitedAccount, Account creditedAccount, BigDecimal amount, BigDecimal amountPaid) {

        Exchange exchange = Exchange.builder()
                .debitedAccount(debitedAccount.getAccountNumber())
                .creditedAccount(creditedAccount.getAccountNumber())
                .debitedAmount(amountPaid)
                .creditedAmount(amount)
                .eventAt(ZonedDateTime.now())
                .build();

        exchangeRepository.save(exchange);
    }

}
