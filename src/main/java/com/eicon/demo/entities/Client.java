package com.eicon.demo.entities;
import javax.persistence.*;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numberControl;
    private String name;
    private String cpf;
    private String address;

    public Client() {
    }

    public Client(Long id, Integer numberControl, String name, String cpf, String address) {
        this.id = id;
        this.numberControl = numberControl;
        this.name = name;
        this.cpf = cpf;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long idClient) {
        this.id = idClient;
    }

    public Integer getNumberControl() {
        return numberControl;
    }

    public void setNumberControl(Integer numberControl) {
        this.numberControl = numberControl;
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
}
