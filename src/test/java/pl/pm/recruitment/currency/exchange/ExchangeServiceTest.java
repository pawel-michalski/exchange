package pl.pm.recruitment.currency.exchange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Mock
    private ExchangeRepository exchangeRepository;

    @InjectMocks
    private ExchangeService exchangeService;

    @Test
    void exchange_shouldThrowBusinessException_whenCurrenciesAreSame() {

        ExchangeDto dto = getExchangeDto(Currency.USD);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> exchangeService.exchange(dto));

        assertEquals("Currency has to not be same type", exception.getMessage());
    }


    @Test
    void exchange_shouldPersistExchange_whenValidInput() {

        ExchangeDto dto = getExchangeDto(Currency.PLN);

        Result result = getResult();

        Client user = new Client();
        user.setId(1);


        ExchangeRateJsonResponse rateResponse = new ExchangeRateJsonResponse();
        ExchangeRateJsonResponse.Rate rate = new ExchangeRateJsonResponse.Rate();
        rate.setMid(BigDecimal.valueOf(4.09));
        rateResponse.setRates(List.of(rate));

        BigDecimal expectedTransferredAmount = dto.getAmountToExchange().multiply(BigDecimal.valueOf(4.09));


        when(accountService.loadUserAccounts(dto)).thenReturn(result.userAccounts);
        when(accountService.loadUser(result.userAccounts)).thenReturn(user);
        when(accountService.getAccountFrom(result.userAccounts, dto.getCurrencyFrom())).thenReturn(result.accountFrom);
        when(accountService.getAccountTo(user, result.userAccounts, dto.getCurrencyTo())).thenReturn(result.accountTo);
        when(exchangeRateService.getExchangeRate(dto.getCurrencyTo())).thenReturn(rateResponse);


        ArgumentCaptor<Exchange> captor = ArgumentCaptor.forClass(Exchange.class);

        exchangeService.exchange(dto);

        verify(exchangeRepository).save(captor.capture());

        Exchange capturedExchange = captor.getValue();

        assertEquals("123", capturedExchange.getDebitedAccount());
        assertEquals("456", capturedExchange.getCreditedAccount());
        assertEquals(dto.getAmountToExchange(), capturedExchange.getCreditedAmount());
        assertEquals(expectedTransferredAmount, capturedExchange.getDebitedAmount());
        assertNotNull(capturedExchange.getEventAt());
    }

    @Test
    void exchange_shouldThrowException_whenNoExchangeRateAvailable() {

        ExchangeDto dto = getExchangeDto(Currency.PLN);

        Set<Account> userAccounts = getResult().userAccounts;
        Client user = new Client();
        user.setId(1);

        when(accountService.loadUserAccounts(dto)).thenReturn(userAccounts);
        when(accountService.loadUser(userAccounts)).thenReturn(user);
        when(accountService.getAccountFrom(userAccounts, dto.getCurrencyFrom())).thenReturn(getResult().accountFrom);
        when(accountService.getAccountTo(user, userAccounts, dto.getCurrencyTo())).thenReturn(getResult().accountTo);

        when(exchangeRateService.getExchangeRate(dto.getCurrencyTo())).thenThrow(CurrencyProviderException.class);


        assertThrows(CurrencyProviderException.class, () -> exchangeService.exchange(dto));
    }

    private static Result getResult() {
        Set<Account> userAccounts = new HashSet<>();
        Account accountFrom = new Account();
        accountFrom.setAccountNumber("123");
        accountFrom.setCurrency(Currency.PLN);
        userAccounts.add(accountFrom);

        Account accountTo = new Account();
        accountTo.setAccountNumber("456");
        accountTo.setCurrency(Currency.USD);
        userAccounts.add(accountTo);
        Result result = new Result(userAccounts, accountFrom, accountTo);
        return result;
    }

    private static class Result {
        public final Set<Account> userAccounts;
        public final Account accountFrom;
        public final Account accountTo;

        public Result(Set<Account> userAccounts, Account accountFrom, Account accountTo) {
            this.userAccounts = userAccounts;
            this.accountFrom = accountFrom;
            this.accountTo = accountTo;
        }
    }

    private static ExchangeDto getExchangeDto(Currency pln) {
        ExchangeDto dto = new ExchangeDto();
        dto.setCurrencyFrom(pln);
        dto.setCurrencyTo(Currency.USD);
        dto.setAmountToExchange(BigDecimal.valueOf(100));
        return dto;
    }

}