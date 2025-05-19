package com.basicauth.basicauth.payment.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TransactionDTO {
    private Long id;
    private Long orderId;
    private Long userId;
    private String productName;
    private Double amount;
    private String status;
    private String paymentMethod;

    public TransactionDTO(Long id, Long orderId, Long userId, String productName, Double amount, String status, String paymentMethod) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.productName = productName;
        this.amount = amount;
        this.status = status;
    }
    
}
