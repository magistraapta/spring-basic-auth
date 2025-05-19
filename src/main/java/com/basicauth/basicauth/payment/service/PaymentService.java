package com.basicauth.basicauth.payment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.basicauth.basicauth.order.entity.Order;
import com.basicauth.basicauth.order.repository.OrderRepository;
import com.basicauth.basicauth.payment.entity.Transaction;
import com.basicauth.basicauth.payment.repository.PaymentRepository;

@Service
public class PaymentService {
    
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public PaymentService(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    public void processPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        // Process payment logic here
        order.setStatus("PAID");
        orderRepository.save(order);

        Transaction transaction = new Transaction();
        transaction.setOrder(order);
        transaction.setUser(order.getUser());
        transaction.setAmount(order.getTotalPrice());
        transaction.setStatus("PAID");
        transaction.setPaymentMethod("CREDIT_CARD");
        paymentRepository.save(transaction);
    }
    
    public List<Transaction> getTransactionsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    public List<Transaction> getTransactionsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}
