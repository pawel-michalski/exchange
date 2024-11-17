package pl.pm.recruitment.currency.exchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createAccount(Client client, BigDecimal amount, Currency currency){
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber(currency));
        account.setBalance(amount);
        account.setUser(client);
        account.setCurrency(currency);
        return accountRepository.save(account);
    }

    public static String generateAccountNumber(Currency currencyCode) {
        Random random = new Random();
        int number = random.nextInt(90000) + 10000;
        return currencyCode.name() + number;
    }

    public Set<Account> findAllAccountsBySameUser(String accountNumber) {
        return accountRepository.findAllAccountsBySameUser(accountNumber);
    }

    public void updateAccount(Account account) {
        accountRepository.save(account);
    }


    public Set<AccountDto> getClientAccounts(int id) {
        return accountRepository.findAccountsByUserId(id)
                .stream()
                .map(AccountDto::toDto)
                .collect(Collectors.toUnmodifiableSet());
    }

    public Set<Account> loadUserAccounts(ExchangeDto toChange) {
        return findAllAccountsBySameUser(toChange.getBankAccount());

    }

    public Client loadUser(Set<Account> userAccounts) {
        return userAccounts.stream()
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika dla podanego konta"))
                .getUser();
    }

    public Account getAccountFrom(Set<Account> userAccounts, Currency currencyFrom) {
        return userAccounts.stream()
                .filter(account -> account.getCurrency().equals(currencyFrom))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Konto źródłowe z walutą " + currencyFrom + " nie istnieje!"));
    }

    public Account getAccountTo(Client user, Set<Account> userAccounts, Currency currencyTo) {
        return  userAccounts.stream()
                .filter(account -> account.getCurrency().equals(currencyTo.toString()))
                .findFirst()
                .orElseGet(() -> createAccount(user, BigDecimal.ZERO, currencyTo));
    }

    public void loadAccounts(ExchangeDto toChange, BigDecimal amountToTransfer, Account accountFrom, Account accountTo) {

        log.error("loadAccounts on {}, {}.", accountFrom, toChange);


        if (accountFrom.getBalance().compareTo(toChange.getAmountToExchange()) < 0) {
            log.error("Insufficient funds for exchange on {}.", accountFrom);
            throw new IllegalStateException("Insufficient funds for exchange on " + accountFrom);
        }

        accountFrom.setBalance(accountFrom.getBalance().subtract(toChange.getAmountToExchange()));
        accountTo.setBalance(accountTo.getBalance().add(amountToTransfer));
        updateAccount(accountFrom);
        updateAccount(accountTo);

    }
}
