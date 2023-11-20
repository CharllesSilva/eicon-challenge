package com.eicon.demo.dto;

import com.eicon.demo.entities.Client;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonPropertyOrder({"id", "number_control", "name", "cpf", "address", "_links"})
public class ClientDTO{

    private Long id;
    @JsonProperty("number_control")
    private Integer numberControl;
    private String name;
    private String cpf;
    private String address;

    public ClientDTO() {
    }

    public ClientDTO(Long id, Integer numberControl, String name, String cpf, String address) {
        this.id = id;
        this.numberControl = numberControl;
        this.name = name;
        this.cpf = cpf;
        this.address = address;
    }

    public ClientDTO(Client client) {
        id = client.getId();
        numberControl = client.getNumberControl();
        name = client.getName();
        cpf = client.getCpf();
        address = client.getAddress();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getNumberControl() {
        return numberControl;
    }

    public void setNumberControl(Integer controlNumber) {
        this.numberControl = controlNumber;
    }
}
