package com.github.gradehub.repositories;

import com.github.gradehub.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Find courses by teacher
    @Query("SELECT c FROM Course c WHERE c.teacher.userId = :userId AND c.teacher.role = 'INSTRUCTOR'")
    List<Course> findCoursesByTeacherId(@Param("userId") Long userId);

    // Find courses by student
    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.userId = :userId AND s.role = 'STUDENT'")
    List<Course> findCoursesByStudentId(@Param("userId") Long userId);

    // Count courses by teacher
    @Query("SELECT COUNT(c) FROM Course c WHERE c.teacher.userId = :userId AND c.teacher.role = 'INSTRUCTOR'")
    Long countCoursesByTeacherId(@Param("userId") Long userId);
}
