package pl.pm.recruitment.currency.adaptersInOutDb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.pm.recruitment.currency.exchange.Account;

import java.util.Set;

interface AccountJpaRepository extends JpaRepository<Account, Integer> {
    @Query("SELECT a FROM Account a WHERE a.user = (SELECT acc.user FROM Account acc WHERE acc.accountNumber = :accountNumber)")
    Set<Account> findAllAccountsBySameUser(@Param("accountNumber") String accountNumber);

    Set<Account> findAccountsByUser_Id(Integer userId);

}
