package pl.pm.recruitment.currency.exchange;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;
    private final AccountService accountService;

    /**
     *
     * @param clientDto
     * @return client account number.
     */
    @Transactional
    public String save(ClientDto clientDto) {

        // bardzo naiwne, powinien być przynajmniej pesel
        if (clientRepository.existsByFirstNameAndLastName(clientDto.getFirstName(), clientDto.getLastName())) {
            log.error("Already existsBy, FirstName {}, LastName {}", clientDto.getFirstName(), clientDto.getLastName());
            throw new ClientAlreadyExistsException();
        } else {
            Client newClient = new Client();
            newClient.setFirstName(clientDto.getFirstName());
            newClient.setLastName(clientDto.getLastName());
            Client savedClient = clientRepository.save(newClient);

            Account account = accountService.createAccount(savedClient, clientDto.getBalance(), Currency.PLN);

            log.info("New client {} created, with account {}.", savedClient, account);

            return account.getAccountNumber();

        }

    }

}

