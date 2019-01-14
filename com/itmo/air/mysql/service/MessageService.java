package com.itmo.air.mysql.service;

import com.itmo.air.mysql.entity.Account;
import com.itmo.air.mysql.entity.Message;
import com.itmo.air.mysql.entity.User;
import com.itmo.air.mysql.repo.AccountRepository;
import com.itmo.air.mysql.repo.MessageRepository;
import com.itmo.air.mysql.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class MessageService {

    public enum Result {
        Wrong_sender,
        No_such_recipient,
        Message_has_been_sent,
        No_such_account,
        Success,
        Unauthorized_403,
        No_such_user
    }

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;

    public Result findMessagesByAccountId(Long userId, String userToken, Account account, Pageable pageable) {
        if (userRepository.findById(userId).isPresent()) {
            User newUser = userRepository.findById(userId).get();
            if (newUser.getToken().equals(userToken)) {
                List<String> ids = new ArrayList<>();
                for (Account acc : newUser.getAccounts()) {
                    ids.add(acc.getNicName());
                }
                if (!ids.contains(account.getNicName())) {
                    return Result.No_such_account;
                } else {
                    Account newAccount = accountRepository.findById(account.getId()).get();
                    if (newAccount.getToken().equals(account.getToken())) {
                        return Result.Success;
                    } else {
                        return Result.Unauthorized_403;
                    }
                }
            } else {
                return Result.Unauthorized_403;
            }
        } else {
            return Result.No_such_user;
        }
    }

    public Result createMessage(Long userId, Long accountId, String userToken, String accountToken, String nicName, Message message, Pageable pageable) {
        if (userRepository.findById(userId).isPresent()) {
            User newUser = userRepository.findById(userId).get();
            if (newUser.getToken().equals(userToken)) {
                List<String> ids = new ArrayList<>();
                for (Account acc : newUser.getAccounts()) {
                    ids.add(acc.getNicName());
                }
                if (!ids.contains(nicName)) {
                    return Result.No_such_account;
                } else {
                    Account newAccount = accountRepository.findById(accountId).get();
                    if (newAccount.getToken().equals(accountToken)) {
                        if (!newAccount.getNicName().equals(message.getFrom())) {
                            return Result.Wrong_sender;
                        } else {
                            for (String rec : message.getTo()) {
                                if (!accountRepository.findByNicName(rec).isPresent()) {
                                    return Result.No_such_recipient;
                                } else {
                                    accountRepository.findByNicName(rec).get().getMessages(pageable).add(message);
                                }
                            }
                            messageRepository.save(message);
                            return Result.Message_has_been_sent;
                        }
                    } else {
                        return Result.Unauthorized_403;
                    }
                }
            } else {
                return Result.Unauthorized_403;
            }
        } else {
            return Result.No_such_user;
        }
    }
}