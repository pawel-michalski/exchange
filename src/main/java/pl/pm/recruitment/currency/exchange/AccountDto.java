package pl.pm.recruitment.currency.exchange;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class AccountDto {
    private Currency currency;
    private BigDecimal balance;
    private String accountNumber;

    static AccountDto toDto(Account account) {
        if (account == null) {
            return null;
        }

        return AccountDto.builder()
                .currency(account.getCurrency())
                .balance(account.getBalance())
                .accountNumber(account.getAccountNumber())
                .build();
    }
}
