package com.itmo.air.mysql.validator;

import com.itmo.air.mysql.entity.AccSecWord;
import com.itmo.air.mysql.entity.User;
import com.itmo.air.mysql.repo.AccountRepository;
import com.itmo.air.mysql.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class DeleteAccValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        AccSecWord accSecWord = (AccSecWord) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "secureWord", "NotEmpty");

        if (!userRepository.findByUserName(accountRepository.findByNicName(accSecWord.getNicName()).get().getUserName()).get().getSecureWord().
                equals(accSecWord.getSecureWord())) {
            errors.rejectValue("secureWord", "WrongSecureWord.accSecWordForm.secureWord");
        }

    }
}
