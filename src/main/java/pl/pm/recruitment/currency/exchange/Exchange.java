package pl.pm.recruitment.currency.exchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Exchange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private ZonedDateTime eventAt;
    private String debitedAccount;
    private BigDecimal debitedAmount;
    private String creditedAccount;
    private BigDecimal creditedAmount;
    private int userId;

    @PersistenceConstructor
    public Exchange() {}
}
