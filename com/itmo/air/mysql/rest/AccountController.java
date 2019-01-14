package com.itmo.air.mysql.rest;

import com.itmo.air.mysql.entity.Account;
import com.itmo.air.mysql.entity.User;
import com.itmo.air.mysql.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/users/{userId}/accounts")
    public Page<Account> getAllAccountsByUserId(@PathVariable(value = "userId") Long userId,
                                                Pageable pageable) {
        return accountService.findByUserId(userId, pageable);
    }

    @PutMapping("/users/{userId}/accounts/login")
    public String logIn(@PathVariable(value = "userId") Long userId,
                        @RequestHeader(value = "token") String token,
                        @Valid @RequestBody Account account) {
        AccountService.Result result = accountService.logIn(userId, token, account);
        switch (result) {
            case Wrong_password:
                return "Wrong account password";
            case No_such_account:
                return "No such account";
            case Success:
                return "Success";
            case No_such_user:
                return "No such user";
            case Unauthorized_403:
                return "Error 403. Unauthorized";
            default:
                return "Unknown error 503";
        }
    }

    @PutMapping("/users/{userId}/accounts/{accountId}")
    public String logOff(@PathVariable(value = "userId") Long userId,
                         @Valid @RequestBody Account account) {
        AccountService.Result result = accountService.logOff(account, userId);
        switch (result) {
            case Success:
                return "Success";
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

    @PostMapping("/users/{userId}/accounts")
    public String createAccount(@PathVariable(value = "userId") Long userId,
                                @RequestHeader(value = "token") String token,
                                @Valid @RequestBody Account account) {
        AccountService.Result result = accountService.createAccount(userId, token, account);
        switch (result) {
            case No_such_user:
                return "No such user";
            case Unauthorized_403:
                return "Error 403. Unauthorized";
            case Account_created:
                return "Account created";
            case Account_already_exist:
                return "Account with the same name already exists";
            default:
                return "Unknown error 503";
        }
    }

    @DeleteMapping("/users/{userId}/accounts/{accountId}/delete")
    public String deleteAccount(@PathVariable(value = "userId") Long userId,
                                @PathVariable(value = "accountId") Long accountId,
                                @RequestHeader(value = "secureWord") String secureWord,
                                @Valid @RequestBody Account account) {
        AccountService.Result result = accountService.delete(userId, accountId, account, secureWord);
        switch (result) {
            case No_such_user:
                return "No such user";
            case No_such_account:
                return "No such account";
            case Wrong_secureWord:
                return "Wrong secure word";
            case Unauthorized_403:
                return "Error 403. Unauthorized";
            case Account_deleted:
                return "Account deleted";
            default:
                return "Unknown error 503";
        }
    }

    @DeleteMapping("/users/{userId}/accounts/delete-all")
    public String deleteAllAccounts(@PathVariable(value = "userId") Long userId,
                                    @Valid @RequestBody User user) {
        AccountService.Result result = accountService.deleteAll(userId, user);
        switch (result) {
            case No_such_user:
                return "No such user";
            case Wrong_secureWord:
                return "Wrong secure word";
            case Unauthorized_403:
                return "Error 403. Unauthorized";
            case All_accounts_deleted:
                return "All user`s accounts deleted";
            default:
                return "Unknown error 503";
        }
    }

    @PutMapping("/users/{userId}/accounts/{accountId}/change-password")
    public String changePassword(@PathVariable(value = "userId") Long userId,
                                 @PathVariable(value = "accountId") Long accountId,
                                 @RequestHeader(value = "secureWord") String secureWord,
                                 @RequestHeader(value = "token") String token,
                                 @Valid @RequestBody Account account) {
        AccountService.Result result = accountService.change(userId, accountId, secureWord, token, account);
        switch (result) {
            case No_such_user:
                return "No such user";
            case No_such_account:
                return "No such account";
            case Wrong_secureWord:
                return "Wrong secure word";
            case Password_changed:
                return "Password successfully changed";
            default:
                return "Unknown error 503";
        }
    }

    @PutMapping("/users/{userId}/accounts/{accountId}/update")
    public String accountUpdate(@PathVariable(value = "userId") Long userId,
                                @PathVariable(value = "accountId") Long accountId,
                                @Valid @RequestBody Account account) {
        AccountService.Result result = accountService.update(userId, accountId, account);
        switch (result) {
            case No_such_user:
                return "No such user";
            case No_such_account:
                return "No such account";
            case Unauthorized_403:
                return "Error 403. Unauthorized";
            case Account_updated:
                return "Account information successfully updated";
            default:
                return "Unknown error 503";
        }
    }
}