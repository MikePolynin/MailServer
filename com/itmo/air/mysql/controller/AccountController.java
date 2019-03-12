package com.itmo.air.mysql.controller;

import com.itmo.air.mysql.entity.AccSecWord;
import com.itmo.air.mysql.entity.Account;
import com.itmo.air.mysql.service.AccountService;
import com.itmo.air.mysql.validator.AccountValidator;
import com.itmo.air.mysql.validator.DeleteAccValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("nicName")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountValidator accountValidator;

    @Autowired
    DeleteAccValidator deleteAccValidator;

    @RequestMapping(value = "/{userName}/create_acc", method = RequestMethod.GET)
    public String registration_acc(Model model) {
        model.addAttribute("accountForm", new Account());
        return "create_acc";
    }

    @RequestMapping(value = "/{userName}/create_acc", method = RequestMethod.POST)
    public String registration_acc(@ModelAttribute("accountForm") Account accountForm,
                                   @PathVariable(value = "userName") String userName,
                                   BindingResult bindingResult) {
        accountValidator.validate(accountForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "create_acc";
        }
        accountService.save(accountForm, userName);
        return "redirect:/welcome";
    }

    @RequestMapping(value = "/{userName}/{nicName}/account", method = RequestMethod.GET)
    public String account(Model model) {
        model.addAttribute("accountForm", new Account());
        return "/account";
    }

    @RequestMapping(value = "/{userName}/{nicName}/account/", method = RequestMethod.POST)
    public String account(@ModelAttribute("accountForm") Account accountForm) {
        return "/account";
    }

    @RequestMapping(value = "/{userName}/{nicName}/delete_acc", method = RequestMethod.GET)
    public String delete_acc(Model model) {
        model.addAttribute("accSecWordForm", new AccSecWord());
        return "delete_acc";
    }

    @RequestMapping(value = "/{userName}/{nicName}/delete_acc", method = RequestMethod.POST)
    public String delete_acc(@ModelAttribute("accSecWordForm") AccSecWord accSecWordForm,
                             @PathVariable(value = "nicName") String nicName,
                             BindingResult bindingResult) {
        deleteAccValidator.validate(accSecWordForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "delete_acc";
        }
        accountService.deleteAcc(nicName);
        return "redirect:/welcome";
    }

    @RequestMapping(value = "/trylaterAcc", method = RequestMethod.GET)
    public String trylaterAcc() {
        return "trylaterAcc";
    }
}