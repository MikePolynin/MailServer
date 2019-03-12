package com.itmo.air.mysql.validator;

import com.itmo.air.mysql.entity.Account;
import com.itmo.air.mysql.entity.User;
import com.itmo.air.mysql.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AccountValidator implements Validator {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Account account = (Account) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nicName", "NotEmpty");

        if (account.getNicName().length() < 6 || account.getNicName().length() > 32) {
            errors.rejectValue("nicName", "Size.accountForm.nicName");
        }

        if (accountRepository.findByNicName(account.getNicName()).isPresent()) {
            errors.rejectValue("nicName", "Duplicate.accountForm.userName");
        }
    }
}
