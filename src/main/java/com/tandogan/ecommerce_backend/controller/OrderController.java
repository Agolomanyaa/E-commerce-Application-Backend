package com.tandogan.ecommerce_backend.controller;

import com.tandogan.ecommerce_backend.dto.request.CreateOrderRequest;
import com.tandogan.ecommerce_backend.dto.response.OrderDto;
import com.tandogan.ecommerce_backend.model.User;
import com.tandogan.ecommerce_backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order") // DÜZELTME: "/api/v1/orders" -> "/api/v1/order"
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@AuthenticationPrincipal User user, @RequestBody CreateOrderRequest request) {
        OrderDto createdOrder = orderService.createOrder(user.getId(), request);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getUserOrders(@AuthenticationPrincipal User user) {
        List<OrderDto> orders = orderService.getOrdersByUserId(user.getId());
        return ResponseEntity.ok(orders);
    }
}