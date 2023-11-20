package com.eicon.demo.services;

import com.eicon.demo.entities.Client;
import com.eicon.demo.entities.Order;
import com.eicon.demo.entities.OrderItem;
import com.eicon.demo.entities.Product;
import com.eicon.demo.repositories.ClientRepository;
import com.eicon.demo.repositories.OrderItemRepository;
import com.eicon.demo.repositories.OrderRepository;
import com.eicon.demo.repositories.ProductRepository;
import com.eicon.demo.services.exceptions.EntityNotFoundException;
import com.eicon.demo.services.exceptions.LimitProductsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.ZoneId.systemDefault;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Test
    public void testFindAll() {
        Order order1 = new Order(1L, new Client(), new ArrayList<>(), Instant.now(), BigDecimal.ZERO);
        Order order2 = new Order(2L, new Client(), new ArrayList<>(), Instant.now(), BigDecimal.ZERO);
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(order1.getId(), result.get(0).getId());
        assertEquals(order2.getId(), result.get(1).getId());
    }

    @Test
    public void testFindById() {
        Long orderId = 1L;
        Order order = new Order(orderId, new Client(), new ArrayList<>(), Instant.now(), BigDecimal.ZERO);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order result = orderService.findById(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
    }

    @Test
    public void testFindByIdNotFound() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> orderService.findById(orderId));

        assertEquals("Order not found with ID: " + orderId, exception.getMessage());
    }

    @Test
    public void testCreateOrderExceedsMaxProductsLimit() {
        Long clientId = 1L;
        List<OrderItem> orderItems = new ArrayList<>();

        for (int i = 1; i <= 11; i++) {
            Product product = new Product((long) i, "Product " + i, new BigDecimal("10.00"));

            orderItems.add(new OrderItem((long) i, null, product, 2));
        }
        Instant registrationDate = Instant.now();

        LimitProductsException exception = assertThrows(LimitProductsException.class,
                () -> orderService.createOrder(clientId, orderItems, registrationDate));

        assertEquals("The maximum number of distinct products allowed per order is 10.", exception.getMessage());
    }


    @Test
    public void testCreateOrderWithEmptyOrderItems() {
        assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(1L, new ArrayList<>(), Instant.now()));
    }

    @Test
    public void testCreateOrderWithDiscount() {
        Long clientId = 1L;
        List<OrderItem> orderItems = new ArrayList<>();
        Product product = new Product(1L, "Product 1", new BigDecimal("10.00"));
        orderItems.add(new OrderItem(1L, null, product, 10));

        Instant registrationDate = Instant.now();
        Client client = new Client(clientId, 12345, "Teste", "123.456.789-01", "123");
        Order savedOrder = new Order(1L, client, orderItems, registrationDate, new BigDecimal("90.00"));

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order order = orderService.createOrder(clientId, orderItems, registrationDate);

        assertNotNull(order);
        assertEquals(savedOrder.getId(), order.getId());
        assertEquals(savedOrder.getClient().getId(), order.getClient().getId());
        assertEquals(savedOrder.getValueTotal(), order.getValueTotal());

    }

    @Test
    public void testDeleteOrder() {
        Long orderId = 1L;
        doNothing().when(orderRepository).deleteById(orderId);

        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }


    @Test
    public void testDeleteOrderNotFound() {
        Long orderId = 1L;

        doThrow(EntityNotFoundException.class).when(orderRepository).deleteById(orderId);

        assertThrows(EntityNotFoundException.class, () -> orderService.deleteOrder(orderId));

    }

    @Test
    public void testFindByDate() {
        Instant registrationDate = Instant.now();
        LocalDate localDate = registrationDate.atZone(systemDefault()).toLocalDate();

        Instant startOfDay = localDate.atStartOfDay(systemDefault()).toInstant();
        Instant endOfDay = localDate.plusDays(1).atStartOfDay(systemDefault()).toInstant();

        Order order = new Order(1L, new Client(), new ArrayList<>(), registrationDate, BigDecimal.ZERO);
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        when(orderRepository.findByRegistrationDateBetween(startOfDay, endOfDay)).thenReturn(orders);

        List<Order> result = orderService.findByDate(registrationDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(order.getId(), result.get(0).getId());
    }

    @Test
    public void testFindByDateNoOrdersFound() {
        Instant registrationDate = Instant.now();
        LocalDate localDate = registrationDate.atZone(systemDefault()).toLocalDate();

        Instant startOfDay = localDate.atStartOfDay(systemDefault()).toInstant();
        Instant endOfDay = localDate.plusDays(1).atStartOfDay(systemDefault()).toInstant();

        when(orderRepository.findByRegistrationDateBetween(startOfDay, endOfDay)).thenReturn(new ArrayList<>());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            orderService.findByDate(registrationDate);
        });

        String expectedMessage = "No orders found with date: " + registrationDate;
        assertEquals(expectedMessage, exception.getMessage());

    }
}
