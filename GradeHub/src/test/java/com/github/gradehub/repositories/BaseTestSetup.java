package com.github.gradehub.repositories;

import com.github.gradehub.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public abstract class BaseTestSetup {

    @Autowired
    protected UsersRepository usersRepository;

    @Autowired
    protected CourseRepository courseRepository;

    @Autowired
    protected AssignmentRepository assignmentRepository;

    @Autowired
    protected GradeRepository gradeRepository;

    protected Users professor;
    protected Users student1;
    protected Users student2;
    protected Course course;
    protected Assignment assignment;
    protected Grade grade1;
    protected Grade grade2;

    @BeforeEach
    public void setup() {
        cleanupDatabase();
        initializeTestData();
    }

    private void cleanupDatabase() {
        gradeRepository.deleteAll();
        assignmentRepository.deleteAll();
        courseRepository.deleteAll();
        usersRepository.deleteAll();
    }

    private void initializeTestData() {
        initializeUsers();
        createCourse();
        createAssignment();
    }


    protected Users createUser(String name, String lastName, String email, Role role, LocalDate dob) {
        return usersRepository.save(
                Users.builder()
                        .personFirstName(name)
                        .personLastName(lastName)
                        .email(email)
                        .role(role)
                        .dob(dob)
                        .password("securepassword")
                        .build()
        );
    }
    private void initializeUsers(){
        professor = createUser("Professor", "Xoo", "professor.x@example.com", Role.INSTRUCTOR, LocalDate.of(1970, 7, 20));
        student1 = createUser("Sam", "Gonzales", "sgonzales@email.com", Role.STUDENT, LocalDate.of(2001, 5, 10));
        student2 = createUser("Emily", "Johnson", "ejohnson@email.com", Role.STUDENT, LocalDate.of(2000, 8, 22));

    }

    protected void createCourse(){
        course = courseRepository.save(
                Course.builder()
                        .courseName("COMP 324")
                        .description("INTRO TO WEB DEVELOPMENT AND DESIGN")
                        .teacher(professor)
                        .students(List.of(student1, student2))
                        .build()
        );
    }

    protected Course createCourse(String courseName, Users teacher, List<Users> students) {
        course = courseRepository.save(
                Course.builder()
                        .courseName(courseName)
                        .teacher(teacher)
                        .students(students)
                        .build()
        );
        return course;
    }
    protected void createAssignment(){
        assignment = assignmentRepository.save(
                Assignment.builder()
                        .assignmentName("Quiz 1")
                        .assignmentWeight(0.20)
                        .assignmentPostedDate(LocalDate.of(2025, 12, 1))
                        .dueDate(LocalDate.of(2026, 2, 1))
                        .feedback("Good work")
                        .course(course)
                        .build()
        );
    }

    public void initializeGrades(){
        grade1 = gradeRepository.save(
                Grade.builder().score(90.00).user(student1).
                        assignment(assignment).
                        build());

        grade2 = gradeRepository.save(
                Grade.builder().score(12.00).user(student2).assignment(assignment).build());
    }






}
