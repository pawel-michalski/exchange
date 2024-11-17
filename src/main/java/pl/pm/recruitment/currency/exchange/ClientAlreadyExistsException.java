package pl.pm.recruitment.currency.exchange;

public class ClientAlreadyExistsException extends RuntimeException {
    public ClientAlreadyExistsException() {
        super("Account dont exists for user");
    }
}
