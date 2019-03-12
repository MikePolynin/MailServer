package com.itmo.air.mysql.service;

import com.itmo.air.mysql.entity.Role;
import com.itmo.air.mysql.entity.User;
import org.springframework.stereotype.Component;

@Component
public class RoleService {

    public User addRoles(User user) {

        Role role_user = new Role();

        user.getRoles().add(role_user);
        role_user.setName("USER");
        role_user.setUser(user);

        if (user.getUserName().contains("Admin")) {

            Role role_admin = new Role();

            user.getRoles().add(role_admin);
            role_admin.setName("ADMIN");
            role_admin.setUser(user);

        }

        return user;
    }
}
