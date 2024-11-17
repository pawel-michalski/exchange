package pl.pm.recruitment.currency.adaptersInEndpoints;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.pm.recruitment.currency.exchange.BusinessException;
import pl.pm.recruitment.currency.exchange.ClientAlreadyExistsException;
import pl.pm.recruitment.currency.exchange.CurrencyProviderException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CurrencyProviderException.class)
    public ResponseEntity<String> handleCurrencyProviderException(CurrencyProviderException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({IllegalStateException.class, ClientAlreadyExistsException.class, BusinessException.class})
    public ResponseEntity<String> handleClientError(ClientAlreadyExistsException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }



}
