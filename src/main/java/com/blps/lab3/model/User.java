package com.blps.lab3.model;

import com.blps.lab3.util.enums.ModeratorPrivileges;
import com.blps.lab3.util.enums.RoleNames;
import com.blps.lab3.util.enums.UserPrivileges;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.*;


@Entity
@Table(name = "superjob_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    private String lastName;
    private Boolean emailSubscription = true;
    @Column(unique = true, nullable = false)
    private String email;

    public Boolean getEmailSubscription() {
        return emailSubscription;
    }

    public void setEmailSubscription(Boolean emailSubscription) {
        this.emailSubscription = emailSubscription;
    }

    @Column(nullable = false)
    @JsonIgnore
    //TODO length
    private String password;

    public Boolean getModerator() {
        return isModerator;
    }

    public void setModerator(Boolean moderator) {
        isModerator = moderator;
    }

    private Boolean isModerator = false;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Balance balance;

    public Collection<Role> getRoles() {
        return roles;
    }

    public String[] getStringRoles(){
        if(isModerator) return new String[]{"MODERATOR"};
        else return new String[]{"USER"};
    }

    public String[] getAuthorities(){
        ArrayList<String> privileges = new ArrayList<>();
        for(UserPrivileges p : UserPrivileges.values()) privileges.add(p.name());

        if (isModerator) {
            for (ModeratorPrivileges p : ModeratorPrivileges.values()) privileges.add(p.name());
        }
        return privileges.toArray(new String[0]);

    }

    public void setRoles(Collection<Role> roles) {
        boolean moderatorRole = false;
        for(Role r : roles){
            if (Objects.equals(r.getName(), RoleNames.MODERATOR.name())) {
                moderatorRole = true;
                break;
            }
        }
        isModerator = moderatorRole;
        this.roles = roles;
    }

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;


    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.balance = new Balance();
        this.balance.setUser(this);
        this.roles = new ArrayList<>();
    }

    public User() {

    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
