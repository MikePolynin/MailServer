package com.itmo.air.mysql.service;

import com.itmo.air.mysql.entity.Account;
import com.itmo.air.mysql.entity.User;
import com.itmo.air.mysql.repo.AccountRepository;
import com.itmo.air.mysql.repo.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleService roleService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AccountService accountService;

    public void save(User user) {
        User finalUser = roleService.addRoles(user);
        finalUser.setPassword(bCryptPasswordEncoder.encode(finalUser.getPassword()));
        finalUser.setDeleted(0);
        userRepository.save(finalUser);
        Account newAccount = new Account();
        newAccount.setNicName(finalUser.getUserName());
        newAccount.setUser(finalUser);
        newAccount.setUserName(user.getUserName());
        newAccount.setDeleted(0);
        accountRepository.save(newAccount);
    }

    public void change(User user) {
        User newUser = userRepository.findByUserName(user.getUserName()).get();
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(newUser);
    }

    public void update(User user, String userName) {
        User newUser = userRepository.findByUserName(userName).get();
        if (!user.getPassword().isEmpty()) {
            newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            newUser.setPasswordConfirm(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        if (!user.getFirstName().isEmpty()) {
            newUser.setFirstName(user.getFirstName());
        }
        if (!user.getLastName().isEmpty()) {
            newUser.setLastName(user.getLastName());
        }
        userRepository.save(newUser);
    }

    public void delete(String userName) {
        User newUser = userRepository.findByUserName(userName).get();
        accountService.deleteAccs(userName);
        newUser.setDeleted(1);
        userRepository.save(newUser);
    }

}