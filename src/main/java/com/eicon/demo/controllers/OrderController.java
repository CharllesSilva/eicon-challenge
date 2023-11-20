package com.eicon.demo.controllers;


import com.eicon.demo.dto.CreateOrderDTO;
import com.eicon.demo.dto.OrderDTO;
import com.eicon.demo.entities.Order;
import com.eicon.demo.services.OrderService;

import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/orders")
@Tag(name = "Orders", description = "Endpoints for Managing orders")
public class OrderController {

    private final ModelMapper modelMapper;
    private final OrderService orderService;

    public OrderController(ModelMapper modelMapper, OrderService orderService) {
        this.modelMapper = modelMapper;
        this.orderService = orderService;
    }


    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Create a new order",
            description = "Creates a new order with the provided details", tags = {"Orders"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order created successfully",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = OrderDTO.class)
                                    )
                            }),
                    @ApiResponse(responseCode = "400", description = "The order item list cannot be empty.", content = @Content),
                    @ApiResponse(responseCode = "400", description = "The maximum number of distinct products allowed per order is 10.", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Client not found with ID: X", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Product not found with ID: X", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content),
            }
    )
    public ResponseEntity<OrderDTO> createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        Order order = orderService.createOrder(createOrderDTO.getIdClient(), createOrderDTO.getOrderItems(), createOrderDTO.getRegistrationDate());
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(orderDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(orderDTO);

    }

    @GetMapping(value = "/{orderId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Find by id order",
            description = "Finds an order by ID",
            tags = {"Orders"},
            responses = {
                    @ApiResponse(
                            description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = OrderDTO.class)
                                    )
                            }),
                    @ApiResponse(responseCode = "404", description = "Client not found with ID: X", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            },
            parameters = {@Parameter(name = "orderId", in = ParameterIn.PATH, description = "ID of the order to be retrieved", example = "123")}
    )
    public ResponseEntity<OrderDTO> findById(@PathVariable Long orderId) {
        Order order = orderService.findById(orderId);
        OrderDTO orderDTO = new OrderDTO(order);

        return ResponseEntity.ok().body(orderDTO);
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Find all orders",
            description = "Returns a list of all orders or orders filtered by date",
            tags = {"Orders"},
            parameters = {
                    @Parameter(name = "date", in = ParameterIn.QUERY,
                            description = "Filter orders by date", example = "2023-01-01T00:00:00Z")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful operation",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = OrderDTO.class)))}),
                    @ApiResponse(responseCode = "404", description = "Orders not found", content = @Content),
                    @ApiResponse(responseCode = "404", description = "No orders found with date: 2023-01-01T00:00:00Z", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content),
            }
    )
    public ResponseEntity<List<OrderDTO>> findAllOrders(
            @RequestParam(name = "date", required = false)
            @ApiParam(value = "Filter orders by date", example = "2023-01-01T00:00:00Z") Instant date) {
        List<Order> orders;

        if (date != null) {
            orders = orderService.findByDate(date);
        } else {
            orders = orderService.findAll();
        }

        List<OrderDTO> orderDTOs = orders.stream()
                .map(order -> {
                    OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
                    return orderDTO;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(orderDTOs);
    }

    @DeleteMapping(value = "/{orderId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Delete an order by ID",
            description = "Deletes an order by the given ID",
            tags = {"Orders"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Order not found with ID: X", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content),
            }
    )
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
