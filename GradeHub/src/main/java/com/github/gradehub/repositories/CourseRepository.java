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
    @Query("SELECT c FROM Course c WHERE c.teacher.usersId = :usersId AND c.teacher.role = 'INSTRUCTOR'")
    List<Course> findCoursesByTeacherId(@Param("usersId") Long usersId);

    // Find courses by student
    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.usersId = :usersId AND s.role = 'STUDENT'")
    List<Course> findCoursesByStudentId(@Param("usersId") Long usersId);

    // Count courses by teacher
    @Query("SELECT COUNT(c) FROM Course c WHERE c.teacher.usersId = :usersId AND c.teacher.role = 'INSTRUCTOR'")
    Long countCoursesByTeacherId(@Param("usersId") Long usersId);




}

