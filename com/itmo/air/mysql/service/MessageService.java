package com.itmo.air.mysql.service;

import com.itmo.air.mysql.entity.Message;
import com.itmo.air.mysql.repo.AccountRepository;
import com.itmo.air.mysql.repo.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class MessageService {

    public enum Result {
        Wrong_sender,
        No_such_recipient,
        Message_has_been_sent
    }

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    AccountRepository accountRepository;


    public Set<Message> findMessagesByAccountId(Long accountId, Pageable pageable) {
        return accountRepository.findById(accountId).get().getMessages(pageable);
    }

    public Result createMessage(Long accountId, Message message, Pageable pageable) {
        if (!accountRepository.findById(accountId).isPresent() |
                !accountRepository.findById(accountId).get().getNicName().equals(message.getFrom())) {
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
    }
}