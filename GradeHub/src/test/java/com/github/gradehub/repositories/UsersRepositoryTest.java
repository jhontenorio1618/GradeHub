package com.github.gradehub.repositories;

import com.github.gradehub.entities.Course;
import com.github.gradehub.entities.Users;
import com.github.gradehub.entities.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setup() {
        courseRepository.deleteAll();
        usersRepository.deleteAll();
    }


    @Test
    public void testFindByEmail() {
        // Arrange: Create and save a user
        Users user = Users.builder()
                .personName("John")
                .personLastName("Doe")
                .email("john.doe@example.com")
                .role(Role.INSTRUCTOR)
                .dob(LocalDate.of(1995, 5, 25))
                .password("securepassword123")
                .build();
        usersRepository.save(user);

        // Act: Call the repository method
        Optional<Users> retrievedUser = usersRepository.findByEmail("john.doe@example.com");

        // Assert: Verify the result
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getPersonName()).isEqualTo("John");
        assertThat(retrievedUser.get().getPersonLastName()).isEqualTo("Doe");
    }

    @Test
    public void testFindByNameAndLastName() {
        // Arrange: Create and save a user
        Users user = Users.builder()
                .personName("Jane")
                .personLastName("Smith")
                .email("jane.smith@example.com")
                .role(Role.INSTRUCTOR)
                .dob(LocalDate.of(1980, 3, 15))
                .password("anothersecurepassword")
                .build();
        usersRepository.save(user);

        // Act: Call the repository method
        Optional<Users> retrievedUser = usersRepository.findByNameAndLastName("Jane", "Smith");

        // Assert: Verify the result
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getEmail()).isEqualTo("jane.smith@example.com");
    }

    @Test
    public void testFindUsersByCourseId() {
        // Arrange: Create and save a teacher
        Users teacher = Users.builder()
                .personName("Professor")
                .personLastName("Xoo")
                .email("professor.x@example.com")
                .role(Role.INSTRUCTOR)
                .dob(LocalDate.of(1970, 7, 20))
                .password("xsecurepassword")
                .build();
        usersRepository.save(teacher);

        // Arrange: Create and save students
        Users student1 = Users.builder()
                .personName("Student")
                .personLastName("One")
                .email("student.one@example.com")
                .role(Role.STUDENT)
                .dob(LocalDate.of(2000, 1, 10))
                .password("studentpassword1")
                .build();

        Users student2 = Users.builder()
                .personName("Student")
                .personLastName("Two")
                .email("student.two@example.com")
                .role(Role.STUDENT)
                .dob(LocalDate.of(2000, 2, 15))
                .password("studentpassword2")
                .build();

        usersRepository.save(student1);
        usersRepository.save(student2);

        // Arrange: Create and save a course
        Course course = Course.builder()
                .courseName("X-Men Studies")
                .description("Learn to use mutant powers responsibly.")
                .teacher(teacher)
                .students(List.of(student1, student2))
                .build();

        // Set the course for each student
        student1.setCourses(List.of(course));
        student2.setCourses(List.of(course));
        courseRepository.save(course);

        // Act: Call the repository method
        List<Users> usersInCourse = usersRepository.findUsersByCourseId(course.getCourseId());

        // Assert: Verify the results
        assertThat(usersInCourse).hasSize(2); // Two students should be enrolled
        assertThat(usersInCourse).extracting("personName")
                .containsExactlyInAnyOrder("Student", "Student");
    }



}
