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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "messages")
    private Set<Account> accounts = new HashSet<>();

    public String getFrom() {
        return from;
    }

    public HashSet<String> getTo() {
        return to;
    }
}