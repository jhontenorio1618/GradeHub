package com.github.gradehub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users",
        indexes = {
                @Index(name = "idx_person_name", columnList = "person_first_name, person_last_name"),
                @Index(name = "idx_email", columnList = "email")
        }
)
public class Users {

    // TODO: Implement password encryption before deployment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true, updatable = false)
    private Long userId;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must only contain letters")
    @Size(min = 2, max = 50)
    @Column(name = "person_first_name", nullable = false)
    private String personFirstName;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must only contain letters")
    @Size(min = 2, max = 50)
    @Column(name = "person_last_name", nullable = false)
    private String personLastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Past(message = "Date of birth must be in the past")
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @NotBlank
    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 12, max = 50)
    @Column(name = "password", nullable = false)
    private String password;


    @ManyToMany(mappedBy = "students", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Course> courses;

    // One-to-Many: Courses the user teaches
    @OneToMany(mappedBy = "teacher", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Course> taughtCourses;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Grade> grades;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<CourseGrade> courseGrades;
}
