package com.github.gradehub.repositories;

import com.github.gradehub.entities.Assignment;
import com.github.gradehub.entities.Course;
import com.github.gradehub.entities.Role;
import com.github.gradehub.entities.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@ActiveProfiles("test")
public class AssignmentRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UsersRepository usersRepository;

    private Course course;
    private Assignment assignment;

    @BeforeEach
    public void setup() {
        usersRepository.deleteAll();
        assignmentRepository.deleteAll();
        courseRepository.deleteAll();

        // Create test data
        course = createCourse();
        assignment = createAssignment(course);
    }

    @Test
    public void testCreateAssignment() {
        validateAssignment(assignment);
        validateCourse(assignment.getCourse());
        validateStudents(assignment.getCourse().getStudents());
    }

    @Test
    public void testFindAssignmentsByCourseId() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("assignmentName").ascending());

         Page<Assignment> assignmentPage = assignmentRepository.findAssignmentsByCourseId(course.getCourseId(), pageable);
        //Page<Assignment> assignmentPage = assignmentRepository.findAssignmentsByCourseId(createCourse().getCourseId(), pageable);
        List<Assignment> assignments = assignmentPage.getContent();

        assertThat(assignments).isNotEmpty();
        assertThat(assignments.get(0).getCourse().getCourseName()).isEqualTo("COMP 324");
    }

    @Test
    public void testFindAssignmentsByName() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("assignmentName").ascending());

        Page<Assignment> assignmentPage = assignmentRepository.findAssignmentsByName("Quiz 1", pageable);
        List<Assignment> assignments = assignmentPage.getContent();

        assertThat(assignments).isNotEmpty();
        assertThat(assignments.get(0).getCourse().getCourseName()).isEqualTo("COMP 324");
    }

    @Test
    public void testFindAssignmentsDueAfter() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("dueDate").descending());

        Page<Assignment> assignmentPage = assignmentRepository.findAssignmentsDueAfter(LocalDate.of(2025, 12, 2), pageable);
        List<Assignment> assignments = assignmentPage.getContent();

        assertThat(assignments).isNotEmpty();
        assertThat(assignments.get(0).getDueDate()).isAfter(LocalDate.of(2025, 12, 2));
    }


    // ======== Helper Methods =========

    private Users createUser(String name, String lastName, String email, Role role, LocalDate dob, String password) {
        return usersRepository.save(
                Users.builder()
                        .personFirstName(name)
                        .personLastName(lastName)
                        .email(email)
                        .role(role)
                        .dob(dob)
                        .password(password)
                        .build()
        );
    }

    private Course createCourse() {
        return courseRepository.save(
                Course.builder()
                        .courseName("COMP 324")
                        .description("INTRO TO WEB DEVELOPMENT AND DESIGN")
                        .teacher(createUser("Professor", "Xoo", "testProfessor@example.com", Role.INSTRUCTOR, LocalDate.of(1970, 7, 20), "password123password123"))
                        .students(List.of(
                                createUser("Sam", "Gonzales", "sgonzales@email.com", Role.STUDENT, LocalDate.of(2001, 5, 10), "password123password123password123"),
                                createUser("Emily", "Johnson", "ejohnson@email.com", Role.STUDENT, LocalDate.of(2000, 8, 22), "securePasspassword123password123!"),
                                createUser("David", "Martinez", "dmartinez@email.com", Role.STUDENT, LocalDate.of(2002, 2, 14), "davidPass99password123password123")
                        ))
                        .assignments(List.of())
                        .build()
        );
    }

    private Assignment createAssignment(Course course) {
        return assignmentRepository.save(
                Assignment.builder()
                        .assignmentName("Quiz 1")
                        .assignmentWeight(0.95)
                        .assignmentPostedDate(LocalDate.of(2025, 12, 1))
                        .dueDate(LocalDate.of(2026, 11, 1))
                        .feedback("GOOD ASSIGNMENT")
                        .course(course)
                        .build()
        );
    }

    private void validateAssignment(Assignment assignment) {
        assertThat(assignment).isNotNull();
        assertThat(assignment.getAssignmentId()).isNotNull();
        assertThat(assignment.getAssignmentName()).isEqualTo("Quiz 1");
        assertThat(assignment.getCourse()).isNotNull();
    }

    private void validateCourse(Course course) {
        assertThat(course).isNotNull();
        assertThat(course.getCourseName()).isEqualTo("COMP 324");
        assertThat(course.getDescription()).isEqualTo("INTRO TO WEB DEVELOPMENT AND DESIGN");
    }

    private void validateStudents(List<Users> students) {
        assertThat(students).isNotEmpty();
        assertThat(students).hasSize(3);

        List<String> studentNames = students.stream()
                .map(Users::getPersonFirstName)
                .toList();

        List<String> studentEmails = students.stream()
                .map(Users::getEmail)
                .toList();

        assertThat(studentNames)
                .containsExactlyInAnyOrder("Sam", "Emily", "David");

        assertThat(studentEmails)
                .containsExactlyInAnyOrder("sgonzales@email.com", "ejohnson@email.com", "dmartinez@email.com");
    }
}
