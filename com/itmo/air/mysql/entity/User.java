package com.itmo.air.mysql.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false, name = "login")
    private String userName;

    @Column(nullable = false, name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = false, name = "secure_word")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String secureWord;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user")
    @JsonManagedReference
    private Set<Account> accounts = new HashSet<>();

    @Column(name = "token")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String token;

    public Long getTokenTime() {
        return tokenTime;
    }

    public void setTokenTime(Long tokenTime) {
        this.tokenTime = tokenTime;
    }

    @Column(name = "tokenTime")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long tokenTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getSecureWord() {
        return secureWord;
    }
}