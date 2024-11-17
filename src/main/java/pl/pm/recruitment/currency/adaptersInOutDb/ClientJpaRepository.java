package pl.pm.recruitment.currency.adaptersInOutDb;



import org.springframework.data.jpa.repository.JpaRepository;
import pl.pm.recruitment.currency.exchange.Client;

import java.util.Optional;

interface ClientJpaRepository extends JpaRepository<Client, Integer> {
    
    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    Optional<Client> findByFirstNameAndLastName(String firstName, String lastName);
}
