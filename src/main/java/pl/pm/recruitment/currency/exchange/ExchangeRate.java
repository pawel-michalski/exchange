package pl.pm.recruitment.currency.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

class ExchangeRate {

    @JsonProperty("effectiveDate")
    private String effectiveDate;

    @JsonProperty("mid")
    private BigDecimal midRate;

    @JsonProperty("code")
    private String code;
}
