package com.eicon.demo.dto;

import com.eicon.demo.entities.Client;
import com.eicon.demo.entities.Order;
import com.eicon.demo.entities.OrderItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@JsonPropertyOrder({"id", "client", "registrationDate", "orderItems", "valueTotal", "_links"})
public class OrderDTO{

    private Long id;
    private Client client;
    @JsonProperty("registration_date")
    private Instant registrationDate;
    @JsonProperty("items")
    private List<OrderItem> orderItems = new ArrayList<>();
    @JsonProperty("total_value")
    private BigDecimal valueTotal;

    public OrderDTO() {
    }

    public OrderDTO(Long id, Client client, Instant dateTimeOrder, List<OrderItem> orderItems, BigDecimal valueTotal) {
        this.id = id;
        this.client = client;
        this.registrationDate = dateTimeOrder;
        this.orderItems = orderItems;
        this.valueTotal = valueTotal;
    }

    public OrderDTO(Order order) {
        id = order.getId();
        client = order.getClient();
        registrationDate = order.getRegistrationDate();
        orderItems = order.getOrderItems();
        valueTotal = order.getValueTotal();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public Instant getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Instant registrationDate) {
        this.registrationDate = registrationDate;
    }


    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getValueTotal() {
        return valueTotal;
    }

    public void setValueTotal(BigDecimal valueTotal) {
        this.valueTotal = valueTotal;
    }
}
