package com.github.gradehub.services;
import com.github.gradehub.dtos.UserUpdateDTO;
import com.github.gradehub.entities.Users;
import com.github.gradehub.repositories.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UsersRepository usersRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private EmailService emailService;

    @Autowired
    public UserService(UsersRepository usersRepository, EmailService emailService) {
        this.usersRepository = usersRepository;
        this.emailService = emailService;
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }


    public Users createUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(emailService.emailGenerated(user));
        return usersRepository.save(user);
    }

    public boolean deleteUsers(Users user ) {
        if (!usersRepository.existsById(user.getUserId())) {
            return false; // No user found to delete
        }
        usersRepository.deleteById(user.getUserId());
        return !usersRepository.existsById(user.getUserId()); // Confirm deletion
    }

    public List<Users> getAllUsers(){
        return usersRepository.findAll();
    }


    public Users getUserByEmail(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    public Page<Users> findUsersByCourseId(long courseId, Pageable pageable) {
        return usersRepository.findUsersByCourseId(courseId, pageable);
    }

    public Optional<Users> editUser(Long userId, UserUpdateDTO updateDTO) {
        if (!usersRepository.existsById(userId)) {
            return Optional.empty();
        }
        return usersRepository.findById(userId).map(user -> {
            boolean updated = false;

            if (updateDTO.getPersonFirstName() != null) {
                user.setPersonFirstName(updateDTO.getPersonFirstName());
                updated = true;
            }
            if (updateDTO.getPersonLastName() != null) {
                user.setPersonLastName(updateDTO.getPersonLastName());
                updated = true;
            }
            if (updateDTO.getEmail() != null) {
                if (!usersRepository.existsByEmail(updateDTO.getEmail())){
                    user.setEmail(updateDTO.getEmail());
                    updated = true;
                }
            }
            if (updateDTO.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(updateDTO.getPassword())); // Encrypt password
                updated = true;
            }
            return updated ? usersRepository.save(user) : user;
        });
    }


}
