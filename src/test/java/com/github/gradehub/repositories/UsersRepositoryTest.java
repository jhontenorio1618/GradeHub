package com.github.gradehub.repositories;
import com.github.gradehub.entities.Course;
import com.github.gradehub.entities.Role;
import com.github.gradehub.entities.RoleType;
import com.github.gradehub.entities.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles("test")
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
    public void roleVerification(){
        Users teacher = createUser("Solo", "Teacher", "soloteacher@example.com", instructorRole, LocalDate.of(1985, 6, 10));
        Users student = createUser("Johnny", "Student", "johnnystudent@example.com",studentRole , LocalDate.of(1985, 6, 10));
        Users admin = createUser("Maria", "admin", "mariaadminf@example.com", adminRole, LocalDate.of(1985, 6, 10));
        Users staff = createUser("Alex", "staff", "alexStaff@example.com", staffRole, LocalDate.of(1985, 6, 10));
        assertThat(teacher.getRole().getRole()).isEqualTo(RoleType.INSTRUCTOR);
        assertThat(student.getRole().getRole()).isEqualTo(RoleType.STUDENT);
        assertThat(admin.getRole().getRole()).isEqualTo(RoleType.ADMIN);
        assertThat(staff.getRole().getRole()).isEqualTo(RoleType.STAFF);
    }


    @Test
    public void testFindUsersByCourseId_NoStudentsInCourse() {
        Users teacher = usersRepository.save(createUser("Solo", "Teacher", "soloteacher@example.com", instructorRole, LocalDate.of(1985, 6, 10)));
        Course emptyCourse = createCourse("Empty Course", teacher, List.of());
        courseRepository.save(emptyCourse);

        List<Users> retrievedUsers = usersRepository.findUsersByCourseId(emptyCourse.getCourseId());

        // Only instructor should be in the course
        assertThat(retrievedUsers).hasSize(0);

    }




}


