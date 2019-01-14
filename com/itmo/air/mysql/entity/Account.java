package com.itmo.air.mysql.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Pageable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false, name = "nic_name")
    private String nicName;

    @Column(nullable = false, name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToMany
    @JoinTable(name = "accountsMessages",
            joinColumns = @JoinColumn(name = "messageId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "accountId", referencedColumnName = "id"))
    private Set<Message> messages = new HashSet<>();

    @Column(name = "token")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String token;

    @Column(name = "tokenTime")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long tokenTime;

    public User getUser() {
        return user;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public long getTokenTime() {
        return tokenTime;
    }

    public void setTokenTime(long tokenTime) {
        this.tokenTime = tokenTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<Message> getMessages(Pageable pageable) {
        return messages;
    }

    public Long getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNicName() {
        return nicName;
    }

    public void setNicName(String nicName) {
        this.nicName = nicName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}