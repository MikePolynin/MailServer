package com.itmo.air.mysql.controller;

import com.itmo.air.mysql.entity.EveryOne;
import com.itmo.air.mysql.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    AdminService adminService;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin(Model model) {
        List<EveryOne> everyOneList = adminService.getEveryOne();
        model.addAttribute("everyOneForm", everyOneList);
        return "admin";
    }
}
