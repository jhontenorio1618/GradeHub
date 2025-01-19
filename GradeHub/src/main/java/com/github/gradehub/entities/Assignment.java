package com.github.gradehub.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Table(name = "assignment")
@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, name = "assignmentID")
    private long assignmentID;

    @NotBlank
    @Column(name = "assignmentName", nullable = false)
    private String assignmentName;

    @Column(name = "assignmentWeight", nullable = false)
    @Min(value = 0, message = "Weight must be at least 0")
    @Max(value = 1, message = "Weight must not exceed 1")
    private double assignmentWeight;

    @NotNull
    @Column(name = "postedDate")
    private LocalDate assignmentPostedDate;

    @NotNull
    @Future
    @Column(name = "dueDate")
    private LocalDate dueDate;

    @Column(name = "feedback", length = 500)
    private String feedBack;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "courseID", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "assignment", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Grade> grades;

}
