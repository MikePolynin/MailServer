package com.itmo.air.mysql.rest;

import com.itmo.air.mysql.entity.Account;
import com.itmo.air.mysql.entity.Message;
import com.itmo.air.mysql.repo.AccountRepository;
import com.itmo.air.mysql.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RestController
public class MessageController {

    @Autowired
    MessageService messageService;
    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/users/{usersId}/accounts/{accountId}/messages")
    public Set<Message> getMessagesByAccountId(@PathVariable(value = "userId") Long userId,
                                               @RequestHeader(value = "token") String userToken,
                                               @Valid @RequestBody Account account,
                                               Pageable pageable) {
        MessageService.Result result = messageService.findMessagesByAccountId(userId, userToken, account, pageable);
        switch (result) {
            case Success:
                return accountRepository.findByNicName(account.getNicName()).get().getMessages(pageable);
            case No_such_account:
                return "No such account";
            case No_such_user:
                return "No such user";
            case Unauthorized_403:
                return "Error 403. Unauthorized";
        }
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