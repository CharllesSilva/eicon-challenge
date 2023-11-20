package com.eicon.demo.services;

import com.eicon.demo.entities.Client;
import com.eicon.demo.repositories.ClientRepository;
import com.eicon.demo.services.exceptions.ControlNumberDuplicateException;
import com.eicon.demo.services.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    public Client createClient(Client client) {
        if (clientRepository.existsByNumberControl(client.getNumberControl())) {
            throw new ControlNumberDuplicateException("The control number "+ client.getNumberControl()+" already exists. Choose another number.");
        }
        return clientRepository.save(client);
    }

    public Client updateClient(Long clientId, Client client) {
        Client existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + clientId));

        if (client.getNumberControl() != null){
            existingClient.setNumberControl(client.getNumberControl());
        }

        if (client.getName() != null) {
            existingClient.setName(client.getName());
        }

        if (client.getCpf() != null) {
            existingClient.setCpf(client.getCpf());
        }

        if (client.getAddress() != null) {
            existingClient.setAddress(client.getAddress());
        }

        return clientRepository.save(existingClient);
    }

}
