package com.blps.lab1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "vacancies")
public class Vacancy {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long authorId;

    @ManyToOne
    @JoinColumn(name = "authorId", insertable = false, updatable = false)
    @JsonIgnore
    private User user;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;

    private boolean published = false;

    private boolean onModeration = false;


    public boolean isOnModeration() {
        return onModeration;
    }

    public void setOnModeration(boolean onModeration) {
        this.onModeration = onModeration;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getAuthorId() {
        return authorId;
    }
}
