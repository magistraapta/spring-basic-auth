package com.basicauth.basicauth.order.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basicauth.basicauth.auth.entity.User;
import com.basicauth.basicauth.auth.repository.AuthRepository;
import com.basicauth.basicauth.auth.security.CustomUserDetails;
import com.basicauth.basicauth.order.dto.OrderDTO;
import com.basicauth.basicauth.order.entity.Order;
import com.basicauth.basicauth.order.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final AuthRepository authRepository;

    public OrderController(OrderService orderService, AuthRepository authRepository) {
        this.orderService = orderService;
        this.authRepository = authRepository;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {

        List<Order> orders = orderService.getAllOrders();
        List<OrderDTO> orderDTOs = orders.stream()
            .map(order -> new OrderDTO(
                order.getId(),
                order.getProduct().getName(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getUser().getId()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(orderDTOs);
    }
    
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = authRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Set the user to the order
        order.setUser(currentUser);
        
        // Set initial status
        order.setStatus("PENDING");

        
        Order createdOrder = orderService.createOrder(order);
        
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getCurrentUserOrders() {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = authRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Order> userOrders = orderService.getCurrentUserOrders(currentUser.getId());
        return ResponseEntity.ok(userOrders);
    }
}
