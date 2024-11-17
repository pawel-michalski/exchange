package pl.pm.recruitment.currency.adaptersInEndpoints;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pm.recruitment.currency.exchange.AccountDto;
import pl.pm.recruitment.currency.exchange.AccountService;
import pl.pm.recruitment.currency.exchange.ClientDto;
import pl.pm.recruitment.currency.exchange.ClientService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;
    private final AccountService accountService;


    public ClientController(ClientService clientService, AccountService accountService) {
        this.clientService = clientService;
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody ClientDto clientDto) {
        String createdAccountNumber = clientService.save(clientDto);
        return ResponseEntity.status(HttpStatus.OK).body(createdAccountNumber);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<AccountDto>> getClientAccounts(@PathVariable("id") int id) {

        if (id <= 0) {
            return ResponseEntity.badRequest()
                    .body(Collections.emptyList());
        }

        Set<AccountDto> clientAccounts = accountService.getClientAccounts(id);
        if (clientAccounts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new ArrayList<>(clientAccounts));
    }

}
