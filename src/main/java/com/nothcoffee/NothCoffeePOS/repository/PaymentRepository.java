package com.nothcoffee.NothCoffeePOS.repository;

import com.nothcoffee.NothCoffeePOS.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
