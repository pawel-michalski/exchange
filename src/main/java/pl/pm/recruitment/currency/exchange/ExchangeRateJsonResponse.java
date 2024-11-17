package pl.pm.recruitment.currency.exchange;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ExchangeRateJsonResponse {
    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;

    @Getter
    @Setter
    public static class Rate {
        private String no;
        private String effectiveDate;
        private BigDecimal mid;
    }
}
