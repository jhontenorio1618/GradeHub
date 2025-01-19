package com.github.gradehub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Table(name = "course")
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "courseID", unique = true, nullable = false)
    private long id;

    @NotBlank
    @Column(name = "courseName", nullable = false)
    private String courseName;

    @Size(max = 500)
    @Column(name = "courseDescription", length = 500)
    private String description;

    // Foreign Key for Teacher
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    // Foreign Keys for Assignments
    @OneToMany(mappedBy = "course", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Assignment> assignments;

}
