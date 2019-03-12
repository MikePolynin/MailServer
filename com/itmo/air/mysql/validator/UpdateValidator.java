package com.itmo.air.mysql.validator;

import com.itmo.air.mysql.entity.User;
import com.itmo.air.mysql.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UpdateValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        if (!user.getPassword().isEmpty()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "NotEmpty");

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "secureWord", "NotEmpty");

            if (!userRepository.findByUserName(user.getUserName()).get().getSecureWord().equals(user.getSecureWord())) {
                errors.rejectValue("secureWord", "WrongSecureWord.userForm.secureWord");
            }

            if (!user.getPasswordConfirm().equals(user.getPassword())) {
                errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
            }
        }





    }
}
