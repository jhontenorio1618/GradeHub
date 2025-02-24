package com.github.gradehub.services;

import com.github.gradehub.entities.Users;
import com.github.gradehub.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    private UsersRepository usersRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);


    public Users createUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return usersRepository.save(user);
    }


}
