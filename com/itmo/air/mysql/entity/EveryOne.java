package com.itmo.air.mysql.entity;

import javax.persistence.*;

public class EveryOne {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userID;

    private String userName;

    private int userDeleted;

    private Long accountID;

    private String nicName;

    private int accountDeleted;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserDeleted() {
        return userDeleted;
    }

    public void setUserDeleted(int userDeleted) {
        this.userDeleted = userDeleted;
    }

    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public String getNicName() {
        return nicName;
    }

    public void setNicName(String nicName) {
        this.nicName = nicName;
    }

    public int getAccountDeleted() {
        return accountDeleted;
    }

    public void setAccountDeleted(int accountDeleted) {
        this.accountDeleted = accountDeleted;
    }
}
