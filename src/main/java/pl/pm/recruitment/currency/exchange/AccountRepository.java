package pl.pm.recruitment.currency.exchange;


import java.util.Set;

/** It is a SPI (Service Provider interface), a Port, for an active side,
 * to hide a db, make an abstraction of it. */
public interface AccountRepository   {

    Set<Account> findAllAccountsBySameUser(String accountNumber);

    Set<Account> findAccountsByUserId(Integer userId);

    Account save(Account account);
}
