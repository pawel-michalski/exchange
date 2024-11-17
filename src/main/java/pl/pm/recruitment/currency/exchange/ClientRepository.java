package pl.pm.recruitment.currency.exchange;


import java.util.Optional;

/** It is a SPI (Service Provider interface), a Port, for an active side,
 * to hide a db, make an abstraction of it. */
public interface ClientRepository  {
    
    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    Client save(Client client);

    Optional<Client> findByFirstNameAndLastName(String firstName, String lastName);
}
