package com.github.gradehub.services;

import com.github.gradehub.entities.Users;
import com.github.gradehub.repositories.UsersRepository;
import org.springframework.stereotype.Service;

/**
 * The purpose of this class is to create an automated email generator.
 */
@Service
public class UsernameGeneratorService {

    public UsersRepository usersRepository;

    public String generateUsername(Users user) {
        char firstNameInitial = user.getPersonFirstName().charAt(0);
        String lastName = user.getPersonLastName();
        String username = firstNameInitial + lastName;
        String email = username + "@gradehub.com";
        int count = 1;
        while(usersRepository.existsByEmail(email)) {
            email = username + count + "@gradehub.com";
            count++;
        }
        return email;
    }
}
