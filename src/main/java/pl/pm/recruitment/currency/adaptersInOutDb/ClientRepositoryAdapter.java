package pl.pm.recruitment.currency.adaptersInOutDb;

import pl.pm.recruitment.currency.exchange.Client;
import pl.pm.recruitment.currency.exchange.ClientRepository;

import java.util.Optional;

public class ClientRepositoryAdapter implements ClientRepository {

    private final ClientJpaRepository clientJpaRepository;

    public ClientRepositoryAdapter(ClientJpaRepository clientJpaRepository) {
        this.clientJpaRepository = clientJpaRepository;
    }

    @Override
    public boolean existsByFirstNameAndLastName(String firstName, String lastName) {
        return clientJpaRepository.existsByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    public Client save(Client client) {
       return   clientJpaRepository.save(client);
    }

    @Override
    public Optional<Client> findByFirstNameAndLastName(String firstName, String lastName) {
        return clientJpaRepository.findByFirstNameAndLastName(firstName, lastName);
    }
}
