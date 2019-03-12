package com.itmo.air.mysql.controller;

import com.itmo.air.mysql.entity.Account;
import com.itmo.air.mysql.entity.User;
import com.itmo.air.mysql.service.AccountService;
import com.itmo.air.mysql.service.SecurityService;
import com.itmo.air.mysql.service.UserService;

import com.itmo.air.mysql.validator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserValidator userValidator;

    @Autowired
    ChangeValidator changeValidator;

    @Autowired
    SecurityService securityService;

    @Autowired
    UpdateValidator updateValidator;

    @Autowired
    DeleteValidator deleteValidator;

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(Model model) {
        model.addAttribute("accountForm", new Account());
        return "/welcome";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.POST)
    public String welcome(@ModelAttribute("accountForm") Account accountForm,
                          RedirectAttributes redirectAttrs) {
        AccountService.Result result = accountService.logIn(accountForm);
        switch (result) {
            case Empty:
                return "redirect:/trylaterAcc";
            case Wrong_account:
                return "redirect:/trylaterAcc";
            case Success:
                String userName = accountForm.getUserName();
                String nicName = accountForm.getNicName();
                redirectAttrs.addFlashAttribute("nicName", nicName);
                return "redirect:/" + userName + "/" + nicName + "/account";
            default:
                return "Unknown error 503";
        }
    }

    @RequestMapping(value = "/trylater", method = RequestMethod.GET)
    public String trylater() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/welcome";
        }
        return "trylater";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userService.save(userForm);
        securityService.autologin(userForm.getUserName(), userForm.getPasswordConfirm());
        return "redirect:/welcome";
    }

    @RequestMapping(value = "/change", method = RequestMethod.GET)
    public String change(Model model) {
        model.addAttribute("userForm", new User());
        return "change";
    }

    @RequestMapping(value = "/change", method = RequestMethod.POST)
    public String change(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        changeValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "change";
        }
        userService.change(userForm);
        return "redirect:/login";
    }

    @RequestMapping(value = "/{userName}/update", method = RequestMethod.GET)
    public String update(Model model) {
        model.addAttribute("userForm", new User());
        return "update";
    }

    @RequestMapping(value = "/{userName}/update", method = RequestMethod.POST)
    public String update(@ModelAttribute("userForm") User userForm,
                         @PathVariable(value = "userName") String userName,
                         BindingResult bindingResult) {
        updateValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "update";
        }
        userService.update(userForm, userName);
        return "redirect:/welcome";
    }

    @RequestMapping(value = "/{userName}/delete", method = RequestMethod.GET)
    public String delete(Model model) {
        model.addAttribute("userForm", new User());
        return "delete";
    }
    @RequestMapping(value = "/{userName}/delete", method = RequestMethod.POST)
    public String delete(@ModelAttribute("userForm") User userForm,
                         @PathVariable(value = "userName") String userName,
                         BindingResult bindingResult) {
        deleteValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "delete";
        }
        userService.delete(userName);
        return "redirect:/login";
    }

    @RequestMapping(value = "/{userName}/delete_accs", method = RequestMethod.GET)
    public String delete_accs(Model model) {
        model.addAttribute("userForm", new User());
        return "delete_accs";
    }

    @RequestMapping(value = "/{userName}/delete_accs", method = RequestMethod.POST)
    public String delete_accs(@ModelAttribute("userForm") User userForm,
                              @PathVariable(value = "userName") String userName,
                              BindingResult bindingResult) {
        deleteValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "delete_accs";
        }
        accountService.deleteAccs(userName);
        return "redirect:/welcome";
    }
}