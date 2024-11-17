package pl.pm.recruitment.currency.exchange;

public class CurrencyProviderException extends RuntimeException {

    public CurrencyProviderException() {
        super("Problem with external resource");
    }
}
