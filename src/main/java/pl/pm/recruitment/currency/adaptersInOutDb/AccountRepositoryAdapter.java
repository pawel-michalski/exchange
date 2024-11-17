package pl.pm.recruitment.currency.adaptersInOutDb;

import pl.pm.recruitment.currency.exchange.Account;
import pl.pm.recruitment.currency.exchange.AccountRepository;

import java.util.Set;

public class AccountRepositoryAdapter implements AccountRepository {

    private final AccountJpaRepository accountJpaRepository;

    public AccountRepositoryAdapter(AccountJpaRepository accountJpaRepository) {
        this.accountJpaRepository = accountJpaRepository;
    }

    @Override
    public Set<Account> findAllAccountsBySameUser(String accountNumber) {
        return accountJpaRepository.findAllAccountsBySameUser(accountNumber);
    }

    @Override
    public Set<Account> findAccountsByUserId(Integer userId) {
        return accountJpaRepository.findAccountsByUser_Id(userId);
    }

    @Override
    public Account save(Account account) {
       return accountJpaRepository.save(account);
    }
}
