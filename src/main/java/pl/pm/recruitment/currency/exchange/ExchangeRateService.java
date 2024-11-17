package pl.pm.recruitment.currency.exchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeRateService {

    private final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);
    private final RestTemplate restTemplate;

    public ExchangeRateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public ExchangeRateJsonResponse getExchangeRate(Currency currency) throws CurrencyProviderException {

        String url = URIGenerator.generateURI(currency);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ExchangeRateJsonResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    ExchangeRateJsonResponse.class
            );

            return response.getBody();
        } catch (Exception e) {
            logger.error("Error external resource {}", e.getMessage());
            throw new CurrencyProviderException();
        }
    }


}
