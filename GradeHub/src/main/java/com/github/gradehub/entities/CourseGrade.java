package com.github.gradehub.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "course_grade",
        indexes = {
                @Index(name = "idx_course_user", columnList = "course_id, user_id", unique = true), // Best for student-course lookups
                @Index(name = "idx_user_id", columnList = "user_id", unique = true)
        }

)
@Entity
public class CourseGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "course_grade_id", nullable = false, updatable = false)
    private long courseGradeId;

    @Column(name = "current_grade")
    private Float currentGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
}
