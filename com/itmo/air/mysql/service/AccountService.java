package com.itmo.air.mysql.service;

import com.itmo.air.mysql.entity.Account;
import com.itmo.air.mysql.entity.GettingAccounts;
import com.itmo.air.mysql.entity.User;
import com.itmo.air.mysql.repo.AccountRepository;
import com.itmo.air.mysql.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        Unauthorized_403,
        No_such_account
    }

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CodeService codeService;

    public GettingAccounts findByUserId(Long userId, Pageable pageable) {
        if (userRepository.findById(userId).isPresent()) {
            GettingAccounts gettingAccounts = new GettingAccounts();
            gettingAccounts.setAccounts(accountRepository.findByUserId(userId, pageable));
            return gettingAccounts;
        } else {
            GettingAccounts gettingAccounts = new GettingAccounts();
            gettingAccounts.setErrors("No such user");
            return gettingAccounts;
        }
    }

    public Result createAccount(Long userId, String token, Account account) {
        if (userRepository.findById(userId).isPresent()) {
            User user = userRepository.findById(userId).get();
            if (token.equals(user.getToken())) {
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

    public Result change(Long userId, Long accountId, String secureWord, String token, Account account) {
        if (userRepository.findById(userId).isPresent()) {
            User newUser = userRepository.findById(userId).get();
            if (token.equals(newUser.getToken())) {
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
                return Result.Unauthorized_403;
            }
        } else {
            return Result.No_such_user;
        }
    }

    public Result update(Long userId, Long accountId, Account account) {
        if (userRepository.findById(userId).isPresent()) {
            User newUser = userRepository.findById(userId).get();
            Account newAccount = accountRepository.findById(accountId).get();
            if (account.getToken().equals(newAccount.getToken())) {
                List<Long> ids = new ArrayList<>();
                for (Account acc : newUser.getAccounts()) {
                    ids.add(acc.getId());
                }
                if (!ids.contains(accountId)) {
                    return Result.No_such_account;
                } else {
                    newAccount.setPassword(codeService.encode(account.getPassword()));
                    accountRepository.save(newAccount);
                    return Result.Account_updated;
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
            User newUser = userRepository.findById(userId).get();
            Account newAccount = accountRepository.findById(accountId).get();
            if (account.getToken().equals(newAccount.getToken())) {
                List<Long> ids = new ArrayList<>();
                for (Account acc : newUser.getAccounts()) {
                    ids.add(acc.getId());
                }
                if (!ids.contains(accountId)) {
                    return Result.No_such_account;
                } else if (!newUser.getSecureWord().equals(secureWord)) {
                    return Result.Wrong_secureWord;
                } else {
                    newUser.getAccounts().remove(newAccount);
                    accountRepository.delete(newAccount);
                    return Result.Account_deleted;
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

    public Result logIn(Long userId, String checkToken, Account account) {
        if (userRepository.findById(userId).isPresent()) {
            User newUser = userRepository.findById(userId).get();
            if (newUser.getToken().equals(checkToken)) {
                List<String> ids = new ArrayList<>();
                for (Account acc : newUser.getAccounts()) {
                    ids.add(acc.getNicName());
                }
                if (!ids.contains(account.getNicName())) {
                    return Result.No_such_account;
                } else {
                    Account newAccount = accountRepository.findByNicName(account.getNicName()).get();
                    if (account.getToken() == null) {
                        if (!newAccount.getPassword().equals(codeService.encode(account.getPassword()))) {
                            return Result.Wrong_password;
                        } else {
                            String token = codeService.encode(account.getNicName() + account.getPassword() + System.currentTimeMillis());
                            newAccount.setToken(token);
                            newAccount.setTokenTime(System.currentTimeMillis());
                            accountRepository.save(newAccount);
                            return Result.Success;
                        }
                    } else {
                        if (account.getToken().equals(newAccount.getToken())) {
                            long milliSecToDay = 1000 * 60 * 60 * 24;
                            if ((System.currentTimeMillis() - account.getTokenTime()) >= milliSecToDay) {
                                return Result.Success;
                            } else {
                                String token = codeService.encode(account.getNicName() + account.getPassword() + System.currentTimeMillis());
                                newAccount.setToken(token);
                                newAccount.setTokenTime(System.currentTimeMillis());
                                accountRepository.save(newAccount);
                                return Result.Success;
                            }
                        } else {
                            if (!newAccount.getPassword().equals(codeService.encode(account.getPassword()))) {
                                return Result.Wrong_password;
                            } else {
                                String token = codeService.encode(account.getNicName() + account.getPassword() + System.currentTimeMillis());
                                newAccount.setToken(token);
                                newAccount.setTokenTime(System.currentTimeMillis());
                                accountRepository.save(newAccount);
                                return Result.Success;
                            }
                        }
                    }
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