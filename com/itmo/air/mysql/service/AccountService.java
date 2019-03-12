package com.itmo.air.mysql.service;

import com.itmo.air.mysql.entity.Account;
import com.itmo.air.mysql.entity.User;
import com.itmo.air.mysql.repo.AccountRepository;
import com.itmo.air.mysql.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountService {

    public enum Result {
        Empty,
        Wrong_account,
        Success
    }

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;

    public Result logIn(Account account) {
        if (account.getNicName().isEmpty()) {
            return Result.Empty;
        } else if (!accountRepository.findByNicName(account.getNicName()).isPresent()) {
            return Result.Wrong_account;
        }
        Account accountLogIn = accountRepository.findByNicName(account.getNicName()).get();
        if (!account.getUserName().equals(accountLogIn.getUserName()) || accountLogIn.getDeleted() == 1) {
            return Result.Wrong_account;
        } else {
            return Result.Success;
        }
    }

    public void deleteAccs(String userName) {
        for (Account acc : accountRepository.findByUserName(userName)) {
            acc.setDeleted(1);
            accountRepository.save(acc);
        }
    }

    public void deleteAcc(String nicName) {
        Account account = accountRepository.findByNicName(nicName).get();
        account.setDeleted(1);
        accountRepository.save(account);
    }

    public void save(Account account, String userName) {
        User user = userRepository.findByUserName(userName).get();
        account.setUser(user);
        account.setUserName(userName);
        account.setDeleted(0);
        accountRepository.save(account);
    }
}