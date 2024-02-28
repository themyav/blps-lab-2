package com.blps.lab1.service;

import com.blps.lab1.model.Balance;
import com.blps.lab1.util.Result;
import com.blps.lab1.repo.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    public final Double PUBLISH_COST = 100.0;
    public final Double MIN_DEPOSIT = 1.0;

    @Autowired
    private BalanceRepository balanceRepository;

    public Result deposit(Long id, Double amount) {
        Balance balance = balanceRepository.findById(id).orElse(null);
        if (balance == null) {
            System.out.print("Нет пользователя");
            return new Result(-1, "Нет такого пользователя");
        }
        balance.setAmount(balance.getAmount() + amount);
        balanceRepository.save(balance);
        return new Result(0, "Успешное выполнение операции");
    }

    public Result withdraw(Long id, Double amount) {
        Balance balance = balanceRepository.findById(id).orElse(null);
        if (balance == null) {
            return new Result(-1, "Нет такого пользователя");
        }
        if (balance.getAmount() >= amount) {
            balance.setAmount(balance.getAmount() - amount);
            balanceRepository.save(balance);
            return new Result(0, "Успешное выполнение операции");
        } else {
            return new Result(1, "Недостаточно средств на балансе");
        }
    }



    public Double check(Long id) {
        Balance balance = balanceRepository.findById(id).orElse(new Balance());
        return balance.getAmount();
    }


}
