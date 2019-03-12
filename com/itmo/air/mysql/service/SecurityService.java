package com.itmo.air.mysql.service;

public interface SecurityService {

    String findLoggedInUserName();

    void autologin(String userName, String password);

}
