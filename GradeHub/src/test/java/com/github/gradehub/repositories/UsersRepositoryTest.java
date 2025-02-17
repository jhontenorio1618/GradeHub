
package com.github.gradehub.repositories;

import com.github.gradehub.entities.Course;
import com.github.gradehub.entities.Role;
import com.github.gradehub.entities.Users;
import org.h2.engine.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UsersRepositoryTest extends BaseTestSetup {

    @BeforeEach
    @Override
    public void setup(){
        super.setup();
    }

    @Test
    public void testFindByEmail() {
        Optional<Users> retrievedUser = usersRepository.findByEmail(professor.getEmail());
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getPersonFirstName()).isEqualTo(professor.getPersonFirstName());
    }

    @Test
    public void testFindByNameAndLastName() {
        Optional<Users> retrievedUser = usersRepository.findByNameAndLastName(student1.getPersonFirstName(), student1.getPersonLastName());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getEmail()).isEqualTo(student1.getEmail());
    }

    @Test
    public void testFindUsersByCourseId() {
        List<Users> usersInCourse = usersRepository.findUsersByCourseId(course.getCourseId());

        assertThat(usersInCourse).hasSize(2);
        assertThat(usersInCourse).extracting("personFirstName")
                .containsExactlyInAnyOrder(student1.getPersonFirstName(), student2.getPersonFirstName());

    }

    @Test
    public void findNonExistingUser(){
        Optional<Users> retrieveNonUsers = usersRepository.findByEmail("nonexistingEmail@domain.com");
        assertFalse(retrieveNonUsers.isPresent());
    }

    @Test
    public void testFindUsersByCourseId_NoStudentsInCourse() {
        Users teacher = createUser("Solo", "Teacher", "soloteacher@example.com", Role.INSTRUCTOR, LocalDate.of(1985, 6, 10));
        Course emptyCourse = createCourse("Empty Course", teacher, List.of()); // No students added
        List<Users> retrievedUsers = usersRepository.findUsersByCourseId(course.getCourseId());
        assertThat(retrievedUsers.size()).isEqualTo(1);
        //ONLY THE INSTRUCTOR IS PRESENT.
    }
}


