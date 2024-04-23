package com.blps.lab3.controller;

import com.blps.lab3.dto.BalanceDepositDTO;
import com.blps.lab3.dto.BalanceDisplayDTO;
import com.blps.lab3.util.enums.Result;
import com.blps.lab3.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @GetMapping("/all")
    private ResponseEntity<List<BalanceDisplayDTO>> getAll(){
        return ResponseEntity.ok(balanceService.getAll());
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<?> deposit(@PathVariable Long id, @RequestBody BalanceDepositDTO balance) {
        if(balance.getUserId() == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Объект баланса не содержит id пользователя");
        if(!Objects.equals(balance.getUserId(), id)) return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id получателей не совпадают");
        Double amount = balance.getAmount();
        if(amount == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Объект баланса не содержит суммы пополнения");
        if(amount < balanceService.MIN_DEPOSIT) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Необходимо внести не менее " + balanceService.MIN_DEPOSIT);
        }
        Result result = balanceService.deposit(id, amount);
        String message = result.getMessage();
        if(result.getCode() == 0) return ResponseEntity.status(HttpStatus.OK).body(message);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);

    }

}

