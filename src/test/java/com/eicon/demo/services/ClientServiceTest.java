package com.eicon.demo.services;

import com.eicon.demo.entities.Client;
import com.eicon.demo.repositories.ClientRepository;
import com.eicon.demo.services.exceptions.ControlNumberDuplicateException;
import com.eicon.demo.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @Test
    public void testCreateClientWhenControlNumberDoesNotExists() {
        Client client = new Client(1L, 123, "Teste", "123.456.789-01", "123");
        when(clientRepository.existsByNumberControl(client.getNumberControl())).thenReturn(false);
        when(clientRepository.save(client)).thenReturn(client);

        Client result = clientService.createClient(client);

        assertEquals(client.getNumberControl(), result.getNumberControl());
        assertEquals(client.getName(), result.getName());
        assertEquals(client.getCpf(), result.getCpf());
        assertEquals(client.getAddress(), result.getAddress());
    }

    @Test
    public void testCreateClientWhenControlNumberExists() {
        Client client = new Client(1L, 123, "Teste", "123.456.789-01", "123");
        when(clientRepository.existsByNumberControl(client.getNumberControl())).thenReturn(true);

        ControlNumberDuplicateException exception = assertThrows(ControlNumberDuplicateException.class,
                () -> clientService.createClient(client));

        assertEquals("The control number " + client.getNumberControl() + " already exists. Choose another number.", exception.getMessage());
    }

    @Test
    public void testUpdateClientWhenClientExists() {
        Long clientId = 1L;
        Client existingClient = new Client(clientId, 123, "Teste", "123.456.789-01", "123");
        Client updatedClient = new Client(clientId, 456, "Teste", "987.654.321-09", "456");
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(existingClient)).thenReturn(existingClient);

        Client result = clientService.updateClient(clientId, updatedClient);

        assertEquals(updatedClient.getName(), result.getName());
        assertEquals(updatedClient.getCpf(), result.getCpf());
        assertEquals(updatedClient.getAddress(), result.getAddress());
    }

    @Test
    public void testUpdateClientWhenClientNotFound() {
        Long clientId = 1L;
        Client updatedClient = new Client(clientId, 456, "Teste", "987.654.321-09", "456");
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> clientService.updateClient(clientId, updatedClient));

        assertEquals("Client not found with ID: " + clientId, exception.getMessage());
    }
}
