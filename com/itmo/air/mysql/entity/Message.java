package com.itmo.air.mysql.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, name = "sender")
    private String from;

    @Column(nullable = false, updatable = false, name = "recipients")
    private HashSet<String> to;

    @Column(updatable = false, name = "text")
    private String text;

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "messages")
    private Set<Account> accounts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public HashSet<String> getTo() {
        return to;
    }

    public void setTo(HashSet<String> to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}