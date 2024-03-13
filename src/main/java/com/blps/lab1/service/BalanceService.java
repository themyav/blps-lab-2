package com.blps.lab1.service;

import com.blps.lab1.model.Balance;
import com.blps.lab1.util.Result;
import com.blps.lab1.repo.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class BalanceService {

    public final Double PUBLISH_COST = 100.0;
    public final Double MIN_DEPOSIT = 1.0;

    @Autowired
    public final TransactionTemplate transactionTemplate;

    @Autowired
    private BalanceRepository balanceRepository;

    public BalanceService(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public Balance get(Long id){
        return balanceRepository.findById(id).orElse(null);
    }

    public Double checkAmount(Long id) {
        Balance balance = balanceRepository.findById(id).orElse(null);
        if(balance == null) return null;
        return balance.getRealAmount();
    }

    public Result exist(Long id){
        if(balanceRepository.findById(id).orElse(null) == null) return Result.USER_NOT_FOUND;
        else return Result.OK;
    }

    public Result deposit(Long id, Double amount) {
        Balance balance = get(id);
        if (balance == null) {
            return Result.USER_NOT_FOUND;
        }
        balance.setRealAmount(balance.getRealAmount() + amount);
        balanceRepository.save(balance);
        return Result.OK;
    }

    public Result withdraw(Long id, Double amount) {
        return transactionTemplate.execute(status -> {
            try {
                Balance balance = get(id);
                if (balance == null) {
                    return Result.USER_NOT_FOUND;
                }
                if (balance.getRealAmount() >= amount) {
                    balance.setRealAmount(balance.getRealAmount() - amount);
                    balanceRepository.save(balance);
                    return Result.OK;
                } else {
                    status.setRollbackOnly();
                    return Result.NOT_ENOUGH_BALANCE;
                }
            } catch (Exception e) {
                status.setRollbackOnly();
                return Result.UNKNOWN_ERROR;
            }
        });
    }

    public Result freeze(Long id, Double amount){
        Balance balance = get(id);
        if(balance == null) return Result.USER_NOT_FOUND;
        if(balance.getFrozenAmount() >= amount){
            balance.setFrozenAmount(balance.getFrozenAmount() - amount);
            balance.setRealAmount(balance.getRealAmount() + amount);
            balanceRepository.save(balance);
            return Result.OK;
        }
        else {
            return Result.NOT_ENOUGH_BALANCE;
        }
    }

    public Result defreeze(Long id, Double amount){
        Balance balance = balanceRepository.findById(id).orElse(null);
        if(balance == null){
            return Result.USER_NOT_FOUND;
        }
        if(balance.getRealAmount() >= amount){
            balance.setRealAmount(balance.getRealAmount() - amount);
            balance.setFrozenAmount(balance.getFrozenAmount() + amount);
            balanceRepository.save(balance);
            return Result.OK;
        }
        else {
            return Result.NOT_ENOUGH_BALANCE;
        }
    }




}
