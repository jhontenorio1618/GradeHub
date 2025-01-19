package com.github.gradehub.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User{

    // TODO: Implement password encryption before deployment
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true, updatable = false, name = "userID")
    private long userID;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must only contain letters")
    @Size(min = 2, max = 50)
    @Column(nullable = false)
    private String personName;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name must only contain letters")
    @Size(min = 2, max = 50)
    @Column(nullable = false)
    private String personLastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Past(message = "Date of birth must be in the past")
    @Column(nullable = false)
    private LocalDate dob;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 12, max = 50)
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Course> courses;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Grade> grades;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<CourseGrade> courseGrade;


}