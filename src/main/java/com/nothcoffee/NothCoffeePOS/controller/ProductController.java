package com.nothcoffee.NothCoffeePOS.controller;

import com.nothcoffee.NothCoffeePOS.model.Product;
import com.nothcoffee.NothCoffeePOS.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product-detail";
        } else {
            model.addAttribute("error", "Ürün bulunamadı.");
            return "error";
        }
    }

    @PostMapping
    public String createProduct(@RequestParam String name, @RequestParam double price, Model model) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        productService.save(product);
        return "redirect:/products";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id, Model model) {
        productService.deleteById(id);
        return "redirect:/products";
    }
}
