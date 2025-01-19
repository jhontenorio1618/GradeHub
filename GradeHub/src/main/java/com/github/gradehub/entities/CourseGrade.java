package com.github.gradehub.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "course_grade")
@Entity
public class CourseGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "courseGradeID", nullable = false, updatable = false)
    private long courseGradeId;

    @Column(name="currentGrade")
    private Float currentGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseID", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false)
    private User user;
}
