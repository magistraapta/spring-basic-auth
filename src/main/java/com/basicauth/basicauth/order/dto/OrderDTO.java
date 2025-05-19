package com.basicauth.basicauth.order.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OrderDTO {
    private Long id;
    private String productName;
    private Double totalPrice;
    private String status;
    private Long userId;

    public OrderDTO(Long id, String productName, Double totalPrice, String status, Long userId) {
        this.id = id;
        this.productName = productName;
        this.totalPrice = totalPrice;
        this.status = status;
        this.userId = userId;
    }
}