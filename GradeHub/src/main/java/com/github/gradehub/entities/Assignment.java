package com.github.gradehub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(
        name = "assignment",
        indexes = {
                @Index(name = "idx_assignment_name", columnList = "assignment_name"), // Search by name
                //@Index(name = "idx_course_id", columnList = "course_id"), // Search assignments by course
                @Index(name = "idx_due_date", columnList = "due_date") // Search by due date
        }
)
@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, name = "assignment_id")
    private long assignmentId;

    @NotBlank
    @Column(name = "assignment_name", nullable = false)
    private String assignmentName;

    @Column(name = "assignment_weight", nullable = false)
    @Min(value = 0, message = "Weight must be at least 0")
    @Max(value = 1, message = "Weight must not exceed 1")
    private double assignmentWeight;

    @NotNull
    @Column(name = "posted_date")
    private LocalDate assignmentPostedDate;

    @NotNull
    @Future
    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "feedback", length = 500)
    private String feedback;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "assignment", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Grade> grades;
}
