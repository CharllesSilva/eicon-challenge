package com.eicon.demo.controllers;


import com.eicon.demo.dto.ClientDTO;
import com.eicon.demo.entities.Client;
import com.eicon.demo.services.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/clients")
@Tag(name = "Clients", description = "Endpoints for Managing clients")
public class ClientController {

    private final ModelMapper modelMapper;
    private final ClientService clientService;

    public ClientController(ModelMapper modelMapper, ClientService clientService) {
        this.modelMapper = modelMapper;
        this.clientService = clientService;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Create a new client",
            description = "Creates a new client with the provided details",
            tags = {"Clients"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Client created successfully",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ClientDTO.class)
                                    )
                            }),
                    @ApiResponse(responseCode = "409", description = "The control number X already exists. Choose another number.", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content),
            }
    )
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        Client client = modelMapper.map(clientDTO, Client.class);
        Client createdClient = clientService.createClient(client);

        ClientDTO createdClientDTO = modelMapper.map(createdClient, ClientDTO.class);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdClientDTO.getId())
                .toUri();

        return ResponseEntity.created(uri).body(createdClientDTO);
    }


    @PatchMapping(value = "/{clientId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Update a client",
            description = "Partially update a client with the provided details", tags = {"Clients"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Client updated successfully",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ClientDTO.class)
                                    )
                            }),
                    @ApiResponse(responseCode = "404", description = "Client not found with ID: X", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content),
            }
    )
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long clientId, @RequestBody ClientDTO clientDTO) {
        Client client = modelMapper.map(clientDTO, Client.class);
        Client updatedClient = clientService.updateClient(clientId, client);

        ClientDTO updatedClientDTO = modelMapper.map(updatedClient, ClientDTO.class);

        return ResponseEntity.ok(updatedClientDTO);
    }


}
