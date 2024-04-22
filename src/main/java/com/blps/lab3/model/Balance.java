package com.blps.lab3.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Balance {

    @Id
    private Long userId;

    private Double realAmount = 0.0;

    private Double frozenAmount = 0.0;

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
