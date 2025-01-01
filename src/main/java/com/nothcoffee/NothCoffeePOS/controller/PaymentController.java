package com.nothcoffee.NothCoffeePOS.controller;

import com.nothcoffee.NothCoffeePOS.model.Payment;
import com.nothcoffee.NothCoffeePOS.service.PaymentService;
import com.nothcoffee.NothCoffeePOS.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String listPayments(Model model) {
        List<Payment> payments = paymentService.findAll();
        model.addAttribute("payments", payments);
        return "payments";
    }

    @GetMapping("/{id}")
    public String getPayment(@PathVariable Long id, Model model) {
        Optional<Payment> payment = paymentService.findById(id);
        if (payment.isPresent()) {
            model.addAttribute("payment", payment.get());
            return "payment-detail";
        } else {
            model.addAttribute("error", "Ödeme bulunamadı.");
            return "error";
        }
    }

    @PostMapping
    public String createPayment(@RequestParam Long orderId, @RequestParam double amount, @RequestParam String paymentMethod, Model model) {
        Payment payment = new Payment();
        payment.setOrder(orderService.findById(orderId).get());
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        paymentService.save(payment);
        return "redirect:/payments";
    }

    @DeleteMapping("/{id}")
    public String deletePayment(@PathVariable Long id, Model model) {
        paymentService.deleteById(id);
        return "redirect:/payments";
    }
}
