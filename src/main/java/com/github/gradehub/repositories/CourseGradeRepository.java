package com.github.gradehub.repositories;

import com.github.gradehub.entities.Course;
import com.github.gradehub.entities.CourseGrade;
import com.github.gradehub.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseGradeRepository extends JpaRepository<CourseGrade, Long> {

    // Get a specific grade for a student in a course
    @Query("SELECT cg FROM CourseGrade cg WHERE cg.course.courseId = :course_id AND cg.user.userId = :student_id")
    CourseGrade findGradeByCourseAndStudent(@Param("course_id") Long courseId, @Param("student_id") Long studentId);

    // Get all grades for a student across courses with pagination
    @Query("SELECT cg FROM CourseGrade cg WHERE cg.user.userId = :student_id")
    Page<CourseGrade> findGradesByStudentId(@Param("student_id") Long studentId, Pageable pageable);

    // Get all grades for a specific course with pagination
    @Query("SELECT cg FROM CourseGrade cg WHERE cg.course.courseId = :course_id")
    Page<CourseGrade> findGradesByCourseId(@Param("course_id") Long courseId, Pageable pageable);

}
