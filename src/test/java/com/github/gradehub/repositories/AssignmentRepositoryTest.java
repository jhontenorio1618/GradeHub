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
public class AssignmentRepositoryTest extends BaseTestSetup{

    @Override
    @BeforeEach
    public void setup(){
        super.setup();
    }

    @Test
    public void testFindAssignmentsByCourseId() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("assignmentName").ascending());
         Page<Assignment> assignmentPage = assignmentRepository.findAssignmentsByCourseId(course.getCourseId(), pageable);
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

    @Test
    public void validateAssignment() {
        assertThat(assignment).isNotNull();
        assertThat(assignment.getAssignmentId()).isNotNull();
        assertThat(assignment.getAssignmentName()).isEqualTo("Quiz 1");
        assertThat(assignment.getCourse()).isNotNull();
    }


    @Test
    public void validateCourse() {
        assertThat(course).isNotNull();
        assertThat(course.getCourseName()).isEqualTo("COMP 324");
        assertThat(course.getDescription()).isEqualTo("INTRO TO WEB DEVELOPMENT AND DESIGN");
    }


    @Test
    public void validateStudents() {
        List<Users> students = List.of(
                createUser("Samuel", "Gonzales", "sgonzales1@email.com", studentRole, LocalDate.of(2001, 5, 10)),
                createUser("Eric", "Johnson", "ejohnson1@email.com", studentRole, LocalDate.of(2000, 8, 22))
        );
        assertThat(students).isNotEmpty();
        assertThat(students).hasSize(2);

        List<String> studentNames = students.stream()
                .map(Users::getPersonFirstName)
                .toList();
        List<String> studentEmails = students.stream()
                .map(Users::getEmail)
                .toList();

        assertThat(studentNames)
                .containsExactlyInAnyOrder("Samuel", "Eric");

        assertThat(studentEmails)
                .containsExactlyInAnyOrder("sgonzales1@email.com", "ejohnson1@email.com");
    }


}
