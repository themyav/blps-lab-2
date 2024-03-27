package com.blps.lab1.dto;

public class BalanceDisplayDTO {
    private Double realAmount;

    public BalanceDisplayDTO(Double realAmount, Double frozenAmount, String email) {
        this.realAmount = realAmount;
        this.frozenAmount = frozenAmount;
        this.email = email;
    }

    private Double frozenAmount;

    public Double getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(Double realAmount) {
        this.realAmount = realAmount;
    }

    public Double getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(Double frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;
}
