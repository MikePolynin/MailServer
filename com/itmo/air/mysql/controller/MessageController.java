package com.itmo.air.mysql.controller;

import com.itmo.air.mysql.entity.Message;
import com.itmo.air.mysql.service.MessageService;
import com.itmo.air.mysql.validator.MessageValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes("nicName")
public class MessageController {

    @Autowired
    MessageService messageService;

    @Autowired
    MessageValidator messageValidator;


    @RequestMapping(value = "/{userName}/{nicName}/get_mes", method = RequestMethod.GET)
    public String get_mes(Model model,
                          @PathVariable(value = "nicName") String nicName,
                          Pageable pageable) {
        List<Message> messages = messageService.getMessages(nicName);
        model.addAttribute("messageForm", messages);
        return "get_mes";
    }

    @RequestMapping(value = "/{userName}/{nicName}/send_mes", method = RequestMethod.GET)
    public String send_mes(Model model) {
        model.addAttribute("sendMesForm", new Message());
        return "send_mes";
    }

    @RequestMapping(value = "/{userName}/{nicName}/send_mes", method = RequestMethod.POST)
    public String send_mes(@ModelAttribute("sendMesForm") Message sendMesForm,
                           @PathVariable(value = "nicName") String nicName,
                           BindingResult bindingResult) {
        messageValidator.validate(sendMesForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "send_mes";
        }
        messageService.sendMessage(sendMesForm, nicName);
        return "redirect:/{userName}/{nicName}/account";
    }
}