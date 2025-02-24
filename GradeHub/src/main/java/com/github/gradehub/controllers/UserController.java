package com.github.gradehub.controllers;

import com.github.gradehub.entities.Users;
import com.github.gradehub.repositories.UsersRepository;
import com.github.gradehub.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {


    @Autowired
    private UserService userService;


    @PostMapping("/createUser")
    public Users createUser(@RequestBody Users user) {
        return userService.createUser(user);
    }




}
