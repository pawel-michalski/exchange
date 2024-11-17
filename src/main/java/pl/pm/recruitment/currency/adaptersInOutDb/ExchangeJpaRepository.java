package pl.pm.recruitment.currency.adaptersInOutDb;



import org.springframework.data.jpa.repository.JpaRepository;
import pl.pm.recruitment.currency.exchange.Exchange;

public interface ExchangeJpaRepository extends JpaRepository<Exchange, Long> {
   
}
