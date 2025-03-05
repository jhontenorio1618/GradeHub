package com.github.gradehub.services;
import com.github.gradehub.dtos.PasswordChangeDTO;
import com.github.gradehub.dtos.UserUpdateDTO;
import com.github.gradehub.entities.Users;
import com.github.gradehub.repositories.CourseRepository;
import com.github.gradehub.repositories.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UsernameGeneratorService usernameGeneratorService;
    private final AuthorizationService authorizationService;
    private final CourseRepository courseRepository;


    @Autowired
    public UserService(UsersRepository usersRepository, UsernameGeneratorService usernameGeneratorService,
                       AuthorizationService authorizationService, CourseRepository courseRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.usernameGeneratorService = usernameGeneratorService;
        this.passwordEncoder = passwordEncoder;
        this.authorizationService = authorizationService;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public Users createUser(Users adminUser, Users user) {
        //BEFORE THE ACTION IS EXECUTED, IT ENSURES THE PERSON CREATING THE NEW USER IS AN ADMIN.
        authorizationService.validateAdmin(adminUser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(usernameGeneratorService.generateUsername(user));
        return usersRepository.save(user);
    }

    @Transactional
    public boolean deleteUsers(Users requestedBy, long userId ) {
        authorizationService.validateAdmin(requestedBy);
        if(!usersRepository.existsById(userId)){
            throw new EntityNotFoundException("The userId " + userId + " does not exist");
        }
        usersRepository.deleteById(userId);
        return !usersRepository.existsById(userId); // Confirm deletion
    }


    public List<Users> getAllUsers(Users requestedBy) {
        authorizationService.validateAdminOrStaff(requestedBy);
        return usersRepository.findAll();
    }

    public long verifyUserIdAndAuthorizeAdminOrStaff(Users requestedBy, long userId) {
        if (!usersRepository.existsById(userId)) {
            throw new EntityNotFoundException("The userId " + userId + " does not exist");
        }
        authorizationService.validateAdminOrStaff(requestedBy);
        return userId; // Just return the userId directly, no need to fetch and extract
    }


    public Users findUserByEmail(Users requestedBy, String email) {
        authorizationService.validateAdminOrStaff(requestedBy);
        authorizationService.validateInstructor(requestedBy);
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    public Page<Users> findUsersByCourseId(Users requestBy,long courseId, Pageable pageable) {
        boolean courseExists = courseRepository.existsById(courseId);
        if(authorizationService.validateInstructor(requestBy)){
            boolean courseProfessor = !courseRepository.findCoursesByTeacherId(requestBy.getUserId()).isEmpty();
            if(courseProfessor && courseExists){
                //ONLY RETURNING LIST OF STUDENTS IF AND ONLY IF THEY ARE AN INSTRUCTOR IN THAT COURSE.
                return courseRepository.findStudentsEnrolledInCourse(courseId, pageable);
            }
            else if(courseExists){
                throw new AccessDeniedException("You do not have permission to access this course");
            }
            throw new EntityNotFoundException("Course ID existence: " + false +
                    ", Permission to access Data:  " + authorizationService.validateInstructor(requestBy));
        }
        else if (authorizationService.validateAdmin(requestBy)){
            return usersRepository.findUsersByCourseId(courseId, pageable);
        }
        throw new AccessDeniedException("You do not have permission to access this course");
    }

    @Transactional
    public Optional<Users> editUser(Users requestBy, Long userId, UserUpdateDTO updateDTO) {
        authorizationService.validateAdmin(requestBy);
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

    @Transactional
    public Users changePassword(Users loggedInUser, Long userIdToChangePassword, PasswordChangeDTO passwordChangeDTO) {

        Users userToUpdate = usersRepository.findById(userIdToChangePassword)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userIdToChangePassword));

        if (!loggedInUser.getUserId().equals(userIdToChangePassword)) {
            throw new AuthorizationDeniedException("You are not authorized to change this password.");
        }

        if (!passwordEncoder.matches(passwordChangeDTO.getOldPassword(), userToUpdate.getPassword())) {
            throw new AccessDeniedException("Incorrect current password.");
        }

        String newHashedPassword = passwordEncoder.encode(passwordChangeDTO.getNewPassword());
        userToUpdate.setPassword(newHashedPassword);

        return usersRepository.save(userToUpdate);

    }




}
