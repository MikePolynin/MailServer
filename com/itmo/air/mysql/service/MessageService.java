package com.itmo.air.mysql.service;

import com.itmo.air.mysql.entity.Message;
import com.itmo.air.mysql.repo.AccountRepository;
import com.itmo.air.mysql.repo.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class MessageService {

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    AccountRepository accountRepository;

    public List<Message> getMessages(String nicName) {
        List<Message> reverseMessages = new ArrayList<>();
        List<Message> messages = new ArrayList<>(accountRepository.findByNicName(nicName).get().getMessages());
        for (int i = 1; i <= messages.size(); i++) {
            reverseMessages.add(messages.get(messages.size() - i));
        }
        return reverseMessages;
    }

    public void sendMessage(Message message, String nicName) {
        message.setFrom(nicName);
        HashSet<String> recipients = message.getTo();
        for (String recipient : recipients) {
            accountRepository.findByNicName(recipient).get().getMessages().add(message);
        }
        messageRepository.save(message);
    }
}