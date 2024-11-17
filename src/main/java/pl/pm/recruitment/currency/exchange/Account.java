package pl.pm.recruitment.currency.exchange;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;
import static java.util.Optional.ofNullable;

@Entity
@Getter
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor
@ToString
public class Account {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    @NotNull
    private Currency currency;
    private BigDecimal balance;
    @Column(unique = true)
    private String accountNumber;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Client user;

    @PersistenceConstructor
    public Account() {
    }

    BigDecimal getBalance() {
        return ofNullable(balance).orElse(BigDecimal.ZERO);
    }

}
