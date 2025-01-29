package com.github.gradehub.repositories;
import com.github.gradehub.entities.Course;
import com.github.gradehub.entities.Users;
import com.github.gradehub.entities.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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

    private Users createUser(String name, String lastName, String email, Role role, LocalDate dob) {
        Users user = Users.builder()
                .personName(name)
                .personLastName(lastName)
                .email(email)
                .role(role)
                .dob(dob)
                .password("securepassword")
                .build();
        return usersRepository.save(user);
    }

    private Course createCourse(String courseName, Users teacher, List<Users> students) {
        Course course = Course.builder()
                .courseName(courseName)
                .description("Learn to use mutant powers responsibly.")
                .teacher(teacher)
                .students(students)
                .build();
        return courseRepository.save(course);
    }

    @Test
    public void testFindUsersByCourseId() {

        Users teacher = createUser("Professor", "Xoo", "professor.x@example.com", Role.INSTRUCTOR, LocalDate.of(1970, 7, 20));
        Users student1 = createUser("Student", "One", "student.one@example.com", Role.STUDENT, LocalDate.of(2000, 1, 10));
        Users student2 = createUser("Student", "Two", "student.two@example.com", Role.STUDENT, LocalDate.of(2000, 2, 15));

        Course course = createCourse("X-Men Studies", teacher, List.of(student1, student2));

        List<Users> usersInCourse = usersRepository.findUsersByCourseId(course.getCourseId());


        assertThat(usersInCourse).hasSize(2);
        assertThat(usersInCourse).extracting("personName")
                .containsExactlyInAnyOrder("Student", "Student");
    }

}
