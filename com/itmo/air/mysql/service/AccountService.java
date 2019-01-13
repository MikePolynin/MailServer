package com.itmo.air.mysql.service;

import com.itmo.air.mysql.entity.Account;
import com.itmo.air.mysql.entity.User;
import com.itmo.air.mysql.repo.AccountRepository;
import com.itmo.air.mysql.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountService {

    public enum Result {
        Account_already_exist,
        Account_created,
        No_such_user,
        Wrong_secureWord,
        Password_changed,
        Account_updated,
        Account_deleted,
        All_accounts_deleted,
        Success,
        Wrong_password,
        Unauthorized_403, No_such_account
    }

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CodeService codeService;

    Long milliSecTo30Day = 2592000000L;

    public Page<Account> findByUserId(Long userId, Pageable pageable) {
        return accountRepository.findByUserId(userId, pageable);
    }

    public Result createAccount(Long userId, String token, Account account) {
        if (userRepository.findById(userId).isPresent()) {
            if (token.equals(userRepository.findById(userId).get().getToken())) {
                User user = userRepository.findById(userId).get();
                if (!accountRepository.findByNicName(account.getNicName()).isPresent()) {
                    account.setPassword(codeService.encode(account.getPassword()));
                    account.setUser(user);
                    accountRepository.save(account);
                    return Result.Account_created;
                } else {
                    return Result.Account_already_exist;
                }
            } else {
                return Result.Unauthorized_403;
            }
        } else {
            return Result.No_such_user;
        }
    }

    public Result change(Long userId, Long accountId, String secureWord, Account account) {
        if (userRepository.findById(userId).isPresent()) {
            User newUser = userRepository.findById(userId).get();
            List<Long> ids = new ArrayList<>();
            for (Account acc : newUser.getAccounts()) {
                ids.add(acc.getId());
            }
            if (!ids.contains(accountId)) {
                return Result.No_such_account;
            } else if (!newUser.getSecureWord().equals(secureWord)) {
                return Result.Wrong_secureWord;
            } else if (accountRepository.findById(accountId).isPresent()) {
                Account newAccount = accountRepository.findById(accountId).get();
                newAccount.setPassword(codeService.encode(account.getPassword()));
                accountRepository.save(newAccount);
                return Result.Password_changed;
            } else {
                return Result.No_such_account;
            }
        } else {
            return Result.No_such_user;
        }
    }

    public Result update(Long userId, Long accountId, Account account) {
        if (userRepository.findById(userId).isPresent()) {
            if (account.getToken().equals(accountRepository.findById(accountId).get().getToken())) {
                User newUser = userRepository.findById(userId).get();
                List<Long> ids = new ArrayList<>();
                for (Account acc : newUser.getAccounts()) {
                    ids.add(acc.getId());
                }
                if (!ids.contains(accountId)) {
                    return Result.No_such_account;
                } else if (accountRepository.findById(accountId).isPresent()) {
                    Account newAccount = accountRepository.findById(accountId).get();
                    newAccount.setPassword(codeService.encode(account.getPassword()));
                    accountRepository.save(newAccount);
                    return Result.Account_updated;
                } else {
                    return Result.No_such_account;
                }
            } else {
                return Result.Unauthorized_403;
            }
        } else {
            return Result.No_such_user;
        }
    }

    public Result delete(Long userId, Long accountId, Account account, String secureWord) {
        if (userRepository.findById(userId).isPresent()) {
            if (account.getToken().equals(accountRepository.findById(accountId).get().getToken())) {
                User newUser = userRepository.findById(userId).get();
                List<Long> ids = new ArrayList<>();
                for (Account acc : newUser.getAccounts()) {
                    ids.add(acc.getId());
                }
                if (!ids.contains(accountId)) {
                    return Result.No_such_account;
                } else if (!newUser.getSecureWord().equals(secureWord)) {
                    return Result.Wrong_secureWord;
                } else if (accountRepository.findById(accountId).isPresent()) {
                    Account newAccount = accountRepository.findById(accountId).get();
                    newUser.getAccounts().remove(newAccount);
                    accountRepository.delete(newAccount);
                    return Result.Account_deleted;
                } else {
                    return Result.No_such_account;
                }
            } else {
                return Result.Unauthorized_403;
            }
        } else {
            return Result.No_such_user;
        }
    }

    public Result deleteAll(Long userId, User user) {
        if (userRepository.findById(userId).isPresent()) {
            User newUser = userRepository.findById(userId).get();
            if (user.getToken().equals(newUser.getToken())) {
                if (!newUser.getSecureWord().equals(user.getSecureWord())) {
                    return Result.Wrong_secureWord;
                } else {
                    for (Account acc : newUser.getAccounts()) {
                        newUser.getAccounts().remove(acc);
                        accountRepository.delete(acc);
                    }
                    return Result.All_accounts_deleted;
                }
            } else {
                return Result.Unauthorized_403;
            }
        } else {
            return Result.No_such_user;
        }
    }

    public Result logIn(Long userId, String token, Account account) {
        if (userRepository.findById(userId).isPresent()) {
            if (userRepository.findById(userId).get().getToken().equals(token)) {
                User newUser = userRepository.findById(userId).get();
                List<String> ids = new ArrayList<>();
                for (Account acc : newUser.getAccounts()) {
                    ids.add(acc.getNicName());
                }
                if (!ids.contains(account.getNicName())) {
                    return Result.No_such_account;
                } else if (accountRepository.findByNicName(account.getNicName()).get().getPassword().equals(codeService.encode(account.getPassword()))) {
                    String accToken = codeService.encode(codeService.encode(account.getNicName() + account.getPassword() + System.currentTimeMillis()));
                    account.setToken(accToken);
                    account.setTokenTime(System.currentTimeMillis());
                    return Result.Success;
                } else {
                    return Result.Wrong_password;
                }
            } else {
                return Result.Unauthorized_403;
            }
        } else {
            return Result.No_such_user;
        }
    }

    public Result logOff(Account account, Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            User newUser = userRepository.findById(userId).get();
            List<String> ids = new ArrayList<>();
            for (Account acc : newUser.getAccounts()) {
                ids.add(acc.getNicName());
            }
            if (!ids.contains(account.getNicName())) {
                return Result.No_such_account;
            } else if (account.getToken().equals(accountRepository.findByNicName(account.getNicName()).get().getToken())) {
                account.setToken(null);
                return Result.Success;
            } else {
                return Result.Unauthorized_403;
            }
        } else {
            return Result.No_such_user;
        }
    }
}