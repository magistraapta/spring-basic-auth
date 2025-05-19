package com.basicauth.basicauth.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.basicauth.basicauth.order.entity.Order;
import com.basicauth.basicauth.order.repository.OrderRepository;
import com.basicauth.basicauth.product.entity.Product;
import com.basicauth.basicauth.product.service.ProductService;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order createOrder(Order order) {
        // Validate user
        if (order.getUser() == null) {
            throw new RuntimeException("User must be authenticated to create an order");
        }

        // Get the product and check stock
        Product product = productService.getProductById(order.getProduct().getId());
        
        if (product.getStock() < order.getQuantity()) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStock() + ", Requested: " + order.getQuantity());
        }
        
        // Update product stock
        product.setStock(product.getStock() - order.getQuantity());
        productService.createProduct(product);
        
        // Set the complete product details to the order
        order.setProduct(product);
        
        // Calculate total price
        order.setTotalPrice(product.getPrice() * order.getQuantity());
        
        // Save and return the order
        return orderRepository.save(order);
    }

    // Get orders for current user
    public List<Order> getCurrentUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
