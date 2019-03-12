package com.itmo.air.mysql.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.domain.Pageable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false, name = "nic_name")
    private String nicName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @Column(nullable = false, updatable = false, name = "userName")
    private String userName;

    @Column(nullable = false, name = "deleted")
    private int deleted;

    @ManyToMany
    @JoinTable(name = "accountsMessages",
            joinColumns = @JoinColumn(name = "accountId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "messageId", referencedColumnName = "id"))
    private List<Message> messages = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }


    public User getUser() {
        return user;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Message> getMessages(Pageable pageable) {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}