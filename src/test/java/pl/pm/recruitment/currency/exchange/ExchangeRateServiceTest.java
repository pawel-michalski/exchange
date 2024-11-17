package pl.pm.recruitment.currency.exchange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Test
    void getExchangeRate_shouldReturnExchangeRateJsonResponse_whenRequestIsSuccessful() {

        Currency testCurrency = Currency.USD;
        String mockUrl = URIGenerator.generateURI(testCurrency);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);


        ExchangeRateJsonResponse mockResponse = new ExchangeRateJsonResponse();
        mockResponse.setTable("A");
        mockResponse.setCurrency("US Dollar");
        mockResponse.setCode("USD");

        ExchangeRateJsonResponse.Rate mockRate = new ExchangeRateJsonResponse.Rate();
        mockRate.setNo("001/A/NBP/2024");
        mockRate.setEffectiveDate("2024-11-17");
        mockRate.setMid(BigDecimal.valueOf(4.23));
        mockResponse.setRates(Collections.singletonList(mockRate));

        ResponseEntity<ExchangeRateJsonResponse> responseEntity =
                new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(mockUrl, HttpMethod.GET, entity, ExchangeRateJsonResponse.class))
                .thenReturn(responseEntity);

        ExchangeRateJsonResponse response = exchangeRateService.getExchangeRate(testCurrency);

        assertNotNull(response);
        assertEquals("A", response.getTable());
        assertEquals("US Dollar", response.getCurrency());
        assertEquals("USD", response.getCode());
        assertNotNull(response.getRates());
        assertEquals(1, response.getRates().size());

        ExchangeRateJsonResponse.Rate rate = response.getRates().get(0);
        assertEquals("001/A/NBP/2024", rate.getNo());
        assertEquals("2024-11-17", rate.getEffectiveDate());
        assertEquals(BigDecimal.valueOf(4.23), rate.getMid());
    }
    @Test
    void getExchangeRate_shouldThrowCurrencyProviderException_whenExceptionOccurs() {

        Currency testCurrency = Currency.USD;
        String mockUrl = URIGenerator.generateURI(testCurrency);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);


        when(restTemplate.exchange(mockUrl, HttpMethod.GET, entity, ExchangeRateJsonResponse.class))
                .thenThrow(new RuntimeException("Unexpected error"));

        Exception exception = assertThrows(CurrencyProviderException.class, () ->
                exchangeRateService.getExchangeRate(testCurrency));

        assertNotNull(exception);
    }
}