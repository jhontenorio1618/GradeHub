package com.github.gradehub.services;

import com.github.gradehub.entities.RoleType;
import com.github.gradehub.entities.Users;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {


    public boolean validateAdmin(Users user){
        return user.getRole().getRole().equals(RoleType.ADMIN);
    }

    public boolean validateStaff(Users user){
        return user.getRole().getRole().equals(RoleType.STAFF);
    }

    public boolean validateInstructor(Users user){
        return user.getRole().getRole().equals(RoleType.INSTRUCTOR);
    }

    public boolean validateStudent(Users user){
        return user.getRole().getRole().equals(RoleType.STUDENT);
    }

    public void validateAdminOrStaff(Users user) {
        if (validateAdmin(user) || validateStaff(user)) {
            return;
        }
        throw new AuthorizationDeniedException("Access denied.");
    }

    public void validateUser(Users user) {
        if (validateAdmin(user) || validateStaff(user) || validateInstructor(user) || validateStudent(user)) {
            return;
        }
        throw new AuthorizationDeniedException("Access denied.");

    }

}

