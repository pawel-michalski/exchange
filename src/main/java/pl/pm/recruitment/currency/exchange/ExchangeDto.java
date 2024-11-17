package pl.pm.recruitment.currency.exchange;

import lombok.Setter;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ExchangeDto {
  
    @NotNull
    private Currency currencyFrom;

    @NotNull
    private Currency currencyTo;

    @NotNull
    @Pattern(
            regexp = "^[a-zA-Z]{2}\\d{3}$",
            message = "Bank account must start with 2 letters followed by 3 digits"
    )
    private String bankAccount;

    @NotNull
    @DecimalMin(value = "0.0", message = "Balance must be greater than or equal to 0")
    private BigDecimal amountToExchange;

}
