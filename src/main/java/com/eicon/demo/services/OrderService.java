package com.eicon.demo.services;

import com.eicon.demo.entities.*;
import com.eicon.demo.repositories.ClientRepository;
import com.eicon.demo.repositories.OrderItemRepository;
import com.eicon.demo.repositories.OrderRepository;
import com.eicon.demo.repositories.ProductRepository;
import com.eicon.demo.services.enums.DiscountType;
import com.eicon.demo.services.exceptions.LimitProductsException;
import com.eicon.demo.services.exceptions.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.eicon.demo.services.enums.DiscountType.NO_DISCOUNT;


@Service
public class OrderService {

    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    public OrderService(ClientRepository clientRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository,
            OrderItemRepository orderItemRepository) {
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    private static final int MAX_PRODUCTS_LIMIT = 10;
    private static final int DEFAULT_QUANTITY = 1;

    public List<Order> findByDate(Instant registrationDate) {
        LocalDate localDate = registrationDate.atZone(ZoneId.systemDefault()).toLocalDate();

        Instant startOfDay = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Order> orders = orderRepository.findByRegistrationDateBetween(startOfDay, endOfDay);

        if (orders == null || orders.isEmpty()) {
            throw new EntityNotFoundException("No orders found with date: " + registrationDate);
        }

        return orders;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long idOrder) {
        return orderRepository.findById(idOrder)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + idOrder));
    }

    public Order createOrder(Long idClient, List<OrderItem> orderItems, Instant registrationDate) {
        validateOrderItems(orderItems);
        validateMaxProducts(orderItems);

        Order order = processOrderItems(orderItems, idClient, registrationDate);
        associateItemsWithOrder(order, orderItems);

        order = orderRepository.save(order);
        order.setOrderItems(orderItemRepository.findByOrder(order));

        return order;
    }


    private Order processOrderItems(List<OrderItem> orderItems, Long idClient, Instant registrationDate) {
        BigDecimal totalValue = BigDecimal.ZERO;
        Integer totalQuantity = 0;
        Client existingClient = getClientById(idClient);

        for (OrderItem orderItem : orderItems) {
            Product existingProduct = getProductById(orderItem.getProduct().getId());

            orderItem.setProduct(existingProduct);
            orderItem.setQuantity(orderItem.getQuantity() == 0 ? DEFAULT_QUANTITY : orderItem.getQuantity());
            totalValue = totalValue.add(calculateTotalValueAllProducts(existingProduct.getId(), orderItem.getQuantity()));
            totalQuantity += orderItem.getQuantity();
        }
        return createAndSaveOrder(existingClient, totalValue, totalQuantity, registrationDate);
    }

    private void associateItemsWithOrder(Order order, List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
        }

        orderItemRepository.saveAll(orderItems);
    }

    private Order createAndSaveOrder(Client client, BigDecimal totalValue, Integer totalQuantity, Instant registrationDate) {
        Order order = new Order();
        order.setClient(client);
        order.setValueTotal(applyDiscount(totalValue, totalQuantity));
        order.setRegistrationDate(registrationDate != null ? registrationDate : Instant.now());
        return orderRepository.save(order);
    }


    private void validateOrderItems(List<OrderItem> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("The order item list cannot be empty.");
        }
    }

    private void validateMaxProducts(List<OrderItem> orderItems) {
        if (orderItems.size() > MAX_PRODUCTS_LIMIT) {
            throw new LimitProductsException("The maximum number of distinct products allowed per order is 10.");
        }
    }

    private BigDecimal calculateTotalValueAllProducts(Long idProduct, Integer quantity) {
        BigDecimal totalValue = BigDecimal.ZERO;
        BigDecimal quantityBigDecimal = new BigDecimal(quantity);

        Product product = getProductById(idProduct);
        totalValue = totalValue.add(product.getPrice().multiply(quantityBigDecimal));

        return totalValue;
    }

    private BigDecimal applyDiscount(BigDecimal totalValue, int quantity) {
        BigDecimal discountMultiplier = (quantity >= 10) ? DiscountType.DISCOUNT_10_PERCENT.getMultiplier() : (quantity >= 5) ? DiscountType.DISCOUNT_5_PERCENT.getMultiplier() : NO_DISCOUNT.getMultiplier();
        return totalValue.multiply(discountMultiplier).setScale(2, RoundingMode.HALF_UP);
    }

    private Client getClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + clientId));
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));
    }

    public void deleteOrder(Long orderId) {
        try {
            orderRepository.deleteById(orderId);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Order not found with ID: " + orderId);
        }
    }
}

