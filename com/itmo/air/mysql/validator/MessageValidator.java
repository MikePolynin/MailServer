package com.itmo.air.mysql.validator;

import com.itmo.air.mysql.entity.Message;
import com.itmo.air.mysql.entity.User;
import com.itmo.air.mysql.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class MessageValidator implements Validator {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Message message = (Message) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "text", "NotEmpty");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "to", "NotEmpty");

        for (String nicName : message.getTo()) {
            if (!accountRepository.findByNicName(nicName).isPresent() ||
                    accountRepository.findByNicName(nicName).get().getDeleted() == 1) {
                errors.rejectValue("to", "WrongRecipient.sendMesForm.to");
            }
        }

        if (message.getText().length()>255) {
            errors.rejectValue("text", "TooLong.sendMesForm.text");
        }
    }
}
