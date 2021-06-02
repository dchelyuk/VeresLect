package com.vereslect.controllers;

import com.vereslect.exception.OrderNotFoundException;
import com.vereslect.models.Order;
import com.vereslect.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private static final Logger LOGGER = Logger.getLogger("MyLogger");

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable(name = "id") Integer idd) {
        try {
            return new ResponseEntity<>(orderService.getOrder(idd), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            LOGGER.severe("Order with " + idd + " id does not exists");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public List<Order> getOrders() {
        return orderService.getOrders();
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {

        if (order.getId() == null) {
            return new ResponseEntity<>(orderService.addOrder(order), HttpStatus.OK);
        }
        LOGGER.severe("Tried to create an order with passed id. Order creation should not use ids");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    public ResponseEntity<Order> updateOrder(@RequestBody Order order) {

        if (order.getId() == null) {
            LOGGER.severe("Can't update order without id - null value was passes instead");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            return new ResponseEntity<>(orderService.updateOrder(order), HttpStatus.OK);
        } catch (OrderNotFoundException exception) {
            LOGGER.severe("Can't update an order with non-existing id");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
