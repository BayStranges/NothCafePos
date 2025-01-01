package com.nothcoffee.NothCoffeePOS.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import com.nothcoffee.NothCoffeePOS.model.Order;
import com.nothcoffee.NothCoffeePOS.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ReceiptService {

    @Autowired
    private OrderService orderService;

    public byte[] generateReceipt(Long orderId) throws IOException {
        Order order = orderService.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);

                contentStream.showText("NothCoffeePOS Fişi");
                contentStream.newLine();
                contentStream.showText("Sipariş ID: " + order.getId());
                contentStream.newLine();
                contentStream.showText("Sipariş Durumu: " + order.getStatus());
                contentStream.newLine();
                contentStream.showText("Toplam: " + order.getTotal());
                contentStream.newLine();
                contentStream.newLine();
                contentStream.showText("Ürünler:");
                contentStream.newLine();

                for (OrderItem item : order.getOrderItems()) {
                    contentStream.showText(item.getProduct().getName() + " - " + item.getQuantity() + " x "
                            + item.getProduct().getPrice() + " = " + item.getPrice());
                    contentStream.newLine();
                }

                contentStream.endText();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }
}
