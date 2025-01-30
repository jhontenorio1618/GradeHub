package com.github.gradehub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(
        name = "course",
        indexes = {
                @Index(name = "idx_course_name", columnList = "course_name"),
                @Index(name = "idx_teacher_id", columnList = "user_id")
        }
)
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "course_id", unique = true, nullable = false)
    private long courseId;

    @NotBlank
    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Size(max = 500)
    @Column(name = "course_description", length = 500)
    private String description;

    // Foreign Key for Teacher
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users teacher;

    @ManyToMany
    @JoinTable(
            name = "course_taker",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Users> students;

    // Foreign Keys for Assignments
    @OneToMany(mappedBy = "course", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Assignment> assignments;
}
