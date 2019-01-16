package com.itmo.air.mysql.rest;

import com.itmo.air.mysql.entity.Account;
import com.itmo.air.mysql.entity.GettingMessages;
import com.itmo.air.mysql.entity.Message;
import com.itmo.air.mysql.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class MessageController {

    @Autowired
    MessageService messageService;

    @GetMapping("/users/{usersId}/accounts/{accountId}/messages")
    public GettingMessages getMessagesByAccountId(@PathVariable(value = "userId") Long userId,
                                                  @RequestHeader(value = "token") String userToken,
                                                  @Valid @RequestBody Account account,
                                                  Pageable pageable) {
        return messageService.findMessagesByAccountId(userId, userToken, account, pageable);
    }

    @PostMapping("/users/{usersId}/accounts/{accountId}/messages/create")
    public String createMessage(@PathVariable(value = "userId") Long userId,
                                @PathVariable(value = "accountId") Long accountId,
                                @RequestHeader(value = "userToken") String userToken,
                                @RequestHeader(value = "accountToken") String accountToken,
                                @RequestHeader(value = "nicName") String nicName,
                                @Valid @RequestBody Message message,
                                Pageable pageable) {
        MessageService.Result result = messageService.createMessage(userId, accountId, userToken, accountToken, nicName, message, pageable);
        switch (result) {
            case Wrong_sender:
                return "Wrong sender nicName";
            case No_such_recipient:
                return "No recipients with such nicName";
            case Message_has_been_sent:
                return "Message has been sent";
            case No_such_user:
                return "No such user";
            case No_such_account:
                return "No such account";
            case Unauthorized_403:
                return "Error 403. Unauthorized";
            default:
                return "Unknown error 503";
        }
    }
}