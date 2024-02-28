package com.blps.lab1.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Balance {

    @Id
    private Long userId;

    private Double amount = 0.0;

    private LocalDateTime timestamp = LocalDateTime.now();

    @OneToOne
    @MapsId
    @JoinColumn(name = "userId")
    private User user;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
