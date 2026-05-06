package com.edu.financeservice.controller;

import com.edu.financeservice.entity.Payment;
import com.edu.financeservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/finance/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(@RequestParam UUID tuitionId, @RequestParam String method) {
        return ResponseEntity.ok(paymentService.processPayment(tuitionId, method));
    }
}
