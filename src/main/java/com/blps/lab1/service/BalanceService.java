package com.blps.lab1.service;

import com.blps.lab1.entity.Balance;
import com.blps.lab1.repo.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;

    public Double deposit(Long id, Double amount) {
        Balance balance = balanceRepository.findById(id).orElse(new Balance());
        balance.setAmount(balance.getAmount() + amount);
        balanceRepository.save(balance);
        return balance.getAmount();
    }

    public Double withdraw(Long id, Double amount) {
        Balance balance = balanceRepository.findById(id).orElse(new Balance());
        if (balance.getAmount() >= amount) {
            balance.setAmount(balance.getAmount() - amount);
            balanceRepository.save(balance);
            return balance.getAmount();
        } else {
            throw new IllegalArgumentException("Insufficient balance");
        }
    }

    public Double check(Long id) {
        Balance balance = balanceRepository.findById(id).orElse(new Balance());
        return balance.getAmount();
    }

}
