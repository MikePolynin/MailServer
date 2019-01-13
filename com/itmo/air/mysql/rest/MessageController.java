package com.itmo.air.mysql.rest;

import com.itmo.air.mysql.entity.Message;
import com.itmo.air.mysql.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
public class MessageController {

    @Autowired
    MessageService messageService;

    @GetMapping("/users/{usersId}/accounts/{accountId}/messages")
    public Set<Message> getMessagesByAccountId(@PathVariable(value = "accountId") Long accountId,
                                               Pageable pageable) {
        return messageService.findMessagesByAccountId(accountId, pageable);
    }

    @PostMapping("/users/{usersId}/accounts/{accountId}/messages/create")
    public String createMessage(@PathVariable(value = "accountId") Long accountId,
                                @Valid @RequestBody Message message,
                                Pageable pageable) {
        MessageService.Result result = messageService.createMessage(accountId, message, pageable);
        switch (result) {
            case Wrong_sender:
                return "Wrong sender nicName";
            case No_such_recipient:
                return "No recipients with such nicName";
            case Message_has_been_sent:
                return "Message has been sent";
            default:
                return "Unknown error 503";
        }
    }
}