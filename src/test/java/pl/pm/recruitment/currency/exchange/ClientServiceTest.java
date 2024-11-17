package pl.pm.recruitment.currency.exchange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private ClientService clientService;

    @Test
    void save_shouldSaveClientAndCreateAccount_whenClientDoesNotExist() {
        // Arrange
        ClientDto clientDto = new ClientDto();
        clientDto.setFirstName("Jane");
        clientDto.setLastName("Doe");
        clientDto.setBalance(BigDecimal.valueOf(1000));

        Client savedClient = new Client();
        savedClient.setId(1);
        savedClient.setFirstName("Jane");
        savedClient.setLastName("Doe");

        Account createdAccount = new Account(
                1,  // id
                Currency.PLN,  // currency
                BigDecimal.valueOf(1000),  // balance
                "1234567890",  // accountNumber
                savedClient  // user
        );

        when(clientRepository.existsByFirstNameAndLastName("Jane", "Doe")).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);
        when(accountService.createAccount(eq(savedClient), eq(BigDecimal.valueOf(1000)), eq(Currency.PLN)))
                .thenReturn(createdAccount);

        // Act
        String accountNumber = clientService.save(clientDto);

        // Assert
        assertEquals("1234567890", accountNumber);

        // Verify client save
        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(clientCaptor.capture());
        Client capturedClient = clientCaptor.getValue();
        assertEquals("Jane", capturedClient.getFirstName());
        assertEquals("Doe", capturedClient.getLastName());

        // Verify account creation
        verify(accountService).createAccount(eq(savedClient), eq(BigDecimal.valueOf(1000)), eq(Currency.PLN));
    }

    @Test
    void save_shouldThrowException_whenClientAlreadyExists() {
        // Arrange
        ClientDto clientDto = new ClientDto();
        clientDto.setFirstName("John");
        clientDto.setLastName("Doe");
        clientDto.setBalance(BigDecimal.valueOf(500));

        when(clientRepository.existsByFirstNameAndLastName("John", "Doe")).thenReturn(true);

        assertThrows(ClientAlreadyExistsException.class, () -> clientService.save(clientDto));

        verify(clientRepository, never()).save(any(Client.class));
        verify(accountService, never()).createAccount(any(Client.class), any(BigDecimal.class), any(Currency.class));
    }


}