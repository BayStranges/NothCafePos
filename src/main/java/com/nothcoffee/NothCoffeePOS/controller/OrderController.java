package com.nothcoffee.NothCoffeePOS.controller;

import com.nothcoffee.NothCoffeePOS.model.Order;
import com.nothcoffee.NothCoffeePOS.model.OrderItem;
import com.nothcoffee.NothCoffeePOS.service.OrderService;
import com.nothcoffee.NothCoffeePOS.service.OrderItemService;
import com.nothcoffee.NothCoffeePOS.service.ProductService;
import com.nothcoffee.NothCoffeePOS.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    @SuppressWarnings("unused")
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final ReceiptService receiptService;

    @Autowired
    public OrderController(OrderService orderService, OrderItemService orderItemService, ProductService productService, ReceiptService receiptService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.productService = productService;
        this.receiptService = receiptService;
    }

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable Long id, Model model) {
        Optional<Order> order = orderService.findById(id);
        if (order.isPresent()) {
            model.addAttribute("order", order.get());
            return "order-detail";
        } else {
            model.addAttribute("error", "Sipariş bulunamadı.");
            return "error";
        }
    }

    @PostMapping
    public String createOrder(@RequestParam List<Long> productIds, @RequestParam List<Integer> quantities, Model model) {
        Order order = new Order();
        order.setStatus("New");

        for (int i = 0; i < productIds.size(); i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(productService.findById(productIds.get(i)).orElseThrow(() -> new IllegalArgumentException("Geçersiz ürün ID'si")));
            orderItem.setQuantity(quantities.get(i));
            orderItem.setPrice(orderItem.getProduct().getPrice() * quantities.get(i));
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        order.setTotal(order.getOrderItems().stream().mapToDouble(OrderItem::getPrice).sum());
        orderService.save(order);

        return "redirect:/orders";
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteById(id);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/receipt")
    public ResponseEntity<byte[]> getReceipt(@PathVariable Long id) {
        try {
            byte[] pdf = receiptService.generateReceipt(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=receipt_" + id + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
