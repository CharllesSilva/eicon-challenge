package com.eicon.demo.dto;

import com.eicon.demo.entities.OrderItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"idClient", "orderItems", "registrationDate"})
public class CreateOrderDTO {

    @JsonProperty("id_client")
    private Long idClient;
    @JsonProperty("items")
    private List<OrderItem> orderItems = new ArrayList<>();
    @JsonProperty("registrations_date")
    private Instant registrationDate;

    public CreateOrderDTO() {
    }

    public CreateOrderDTO(Long idClient, List<OrderItem> orderItems, Instant registrationDate) {
        this.idClient = idClient;
        this.orderItems = orderItems;
        this.registrationDate = registrationDate;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Instant getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
    }
}
