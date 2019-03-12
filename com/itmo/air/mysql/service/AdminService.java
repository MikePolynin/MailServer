package com.itmo.air.mysql.service;

import com.itmo.air.mysql.entity.Account;
import com.itmo.air.mysql.entity.EveryOne;
import com.itmo.air.mysql.entity.User;
import com.itmo.air.mysql.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdminService {

    @Autowired
    UserRepository userRepository;

    public List<EveryOne> getEveryOne() {
        List<EveryOne> everyOneList = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            for (Account account : user.getAccounts()) {
                EveryOne unit = new EveryOne();
                unit.setUserID(user.getId());
                unit.setUserName(user.getUserName());
                unit.setUserDeleted(user.getDeleted());
                unit.setAccountID(account.getId());
                unit.setNicName(account.getNicName());
                unit.setAccountDeleted(account.getDeleted());
                everyOneList.add(unit);
            }
        }
        return everyOneList;
    }
}
