package com.blps.lab1.service;

import com.blps.lab1.model.Balance;
import com.blps.lab1.dto.BalanceDisplayDTO;
import com.blps.lab1.repo.UserRepository;
import com.blps.lab1.util.Result;
import com.blps.lab1.repo.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BalanceService {

    public final Double PUBLISH_COST = 100.0;
    public final Double MIN_DEPOSIT = 1.0;

    @Autowired
    public final TransactionTemplate transactionTemplate;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private UserRepository userRepository;

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

    public List<BalanceDisplayDTO> getAll(){
        List<Balance> balances = balanceRepository.findAll();
        List<BalanceDisplayDTO> dtos = new ArrayList<>();
        for(Balance b : balances){
            dtos.add(new BalanceDisplayDTO(b.getRealAmount(), b.getFrozenAmount(), Objects.requireNonNull(userRepository.findById(b.getUserId()).orElse(null)).getEmail()));
        }
        return dtos;
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

    public Result defreeze(Long id, Double amount){
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
}
