package com.blps.lab1.controller;

import com.blps.lab1.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @PostMapping("/{id}/deposit/{amount}")
    public Double deposit(@PathVariable Long id, @PathVariable Double amount) {
        return balanceService.deposit(id, amount);
    }

    @PostMapping("/{id}/withdraw/{amount}")
    public Double withdraw(@PathVariable Long id, @PathVariable Double amount) {
        return balanceService.withdraw(id, amount);
    }

    @GetMapping("/{id}/check")
    public Double check(@PathVariable Long id) {
        return balanceService.check(id);
    }
}

