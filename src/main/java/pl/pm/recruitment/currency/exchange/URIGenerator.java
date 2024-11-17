package pl.pm.recruitment.currency.exchange;

class URIGenerator {
    private final static String PATTERN = "https://api.nbp.pl/api/exchangerates/rates/A/";

    static String generateURI(Currency currency) {
        return PATTERN + currency.name();
    }
}
