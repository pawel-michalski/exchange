package pl.pm.recruitment.currency.adaptersInEndpoints;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pm.recruitment.currency.exchange.ExchangeDto;
import pl.pm.recruitment.currency.exchange.ExchangeService;

@RestController
@RequestMapping("/exchanges")
public class ExchangeController {
    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody ExchangeDto exchangeDto) {
        exchangeService.exchange(exchangeDto);
        return ResponseEntity.ok().build();
    }

}
