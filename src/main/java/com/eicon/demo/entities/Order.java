package com.eicon.demo.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_table")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
    private Instant registrationDate;
    private BigDecimal valueTotal;

    public Order() {
    }

    public Order(Long id, Client client, List<OrderItem> orderItems, Instant registrationDate, BigDecimal valueTotal) {
        this.id = id;
        this.client = client;
        this.orderItems = orderItems;
        this.registrationDate = registrationDate;
        this.valueTotal = valueTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long idOrder) {
        this.id = idOrder;
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

    public void setRegistrationDate(Instant dateTimeOrder) {
        this.registrationDate = dateTimeOrder;
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
