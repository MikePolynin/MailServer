package com.itmo.air.mysql.rest;

import com.itmo.air.mysql.entity.User;
import com.itmo.air.mysql.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public Page<User> getAllUsers(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @PutMapping("/users/login")
    public String logIn(@Valid @RequestBody User user) {
        UserService.Result result = userService.logIn(user);
        switch (result) {
            case Wrong_password:
                return "Wrong user password";
            case Success:
                return "Success";
            case No_such_user:
                return "No such user";
            default:
                return "Unknown error 503";
        }
    }

    @PutMapping("/users/{userId}")
    public String logOff(@Valid @RequestBody User user) {
        UserService.Result result = userService.logOff(user);
        switch (result) {
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

    @PostMapping("/users")
    public String createUser(@Valid @RequestBody User user) {
        UserService.Result result = userService.save(user);
        switch (result) {
            case User_created:
                return "User and default account created";
            case User_already_exist:
                return "User with the same name already exists";
            default:
                return "Unknown error 503";
        }
    }

    @PutMapping("/users/{userId}/change-password")
    public String changePassword(@PathVariable(value = "userId") Long userId,
                                 @Valid @RequestBody User user) {
        UserService.Result result = userService.change(userId, user);
        switch (result) {
            case Wrong_secureWord:
                return "Wrong secure word";
            case Password_changed:
                return "Password successfully changed";
            case No_such_user:
                return "No such user";
            default:
                return "Unknown error 503";
        }
    }

    @PutMapping("/users/{userId}/update")
    public String updateUser(@PathVariable(value = "userId") Long userId,
                             @Valid @RequestBody User user) {
        UserService.Result result = userService.update(userId, user);
        switch (result) {
            case No_such_user:
                return "No such user";
            case User_updated:
                return "User information successfully updated";
            case Unauthorized_403:
                return "Error 403. Unauthorized";
            default:
                return "Unknown error 503";
        }
    }

    @DeleteMapping("/users/{userId}/delete")
    public String deleteUser(@PathVariable(value = "userId") Long userId,
                             @Valid @RequestBody User user) {
        UserService.Result result = userService.delete(userId, user);
        switch (result) {
            case No_such_user:
                return "Wrong user";
            case Wrong_secureWord:
                return "Wrong secure word";
            case User_deleted:
                return "User and all user`s accounts deleted";
            case Unauthorized_403:
                return "Error 403. Unauthorized";
            default:
                return "Unknown error 503";
        }
    }
}