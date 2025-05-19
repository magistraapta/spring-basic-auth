package com.basicauth.basicauth.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.basicauth.basicauth.payment.entity.Transaction;


@Repository
public interface PaymentRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByOrderId(Long orderId);
}
