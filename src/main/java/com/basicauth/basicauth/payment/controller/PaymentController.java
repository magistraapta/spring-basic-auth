package com.basicauth.basicauth.payment.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.basicauth.basicauth.payment.dto.TransactionDTO;
import com.basicauth.basicauth.payment.entity.Transaction;
import com.basicauth.basicauth.payment.service.PaymentService;


@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions(@RequestParam Long userId) {
        List<Transaction> transactions = paymentService.getTransactionsByUserId(userId);

        // mapping from entity to DTO
        List<TransactionDTO> transactionDTOs = transactions.stream()
        .map(tx -> {
            String productName = null;
            if (tx.getOrder() != null && tx.getOrder().getProduct() != null) {
                productName = tx.getOrder().getProduct().getName();
            }

            return new TransactionDTO(
                tx.getId(),
                tx.getOrder().getId(),
                tx.getUser().getId(),
                productName,
                tx.getAmount(),
                tx.getStatus(),
                tx.getPaymentMethod()
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(transactionDTOs);
    }

    @GetMapping("/transactions/order")
    public ResponseEntity<List<Transaction>> getTransactionsByOrderId(@RequestParam Long orderId) {
        List<Transaction> transactions = paymentService.getTransactionsByOrderId(orderId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestBody Long orderId) {
        paymentService.processPayment(orderId);
        return ResponseEntity.ok("Payment processed successfully");
    }
}
