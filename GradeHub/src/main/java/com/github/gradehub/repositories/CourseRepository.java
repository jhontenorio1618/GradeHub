package com.github.gradehub.repositories;

import com.github.gradehub.entities.Course;
import com.github.gradehub.entities.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Find courses by student (and role)
    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.userId = :userId AND s.role.role = :roleType")
    List<Course> findCoursesByStudentId(@Param("userId") Long userId);

    // Find courses by student (string version)
    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.userId = :userId AND s.role.role = 'STUDENT'")
    List<Course> findCoursesByStudentIdString(@Param("userId") Long userId);

    //Find all courses by student.
    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.userId = :userId")
    List<Course> findAllCoursesByStudentId(@Param("userId") Long userId);

    // Count courses by taught by a specific teacher
    @Query("SELECT COUNT(c) FROM Course c WHERE c.teacher.userId = :teacherId")
    Long countCoursesByTeacherId(@Param("teacherId") Long teacherId);

    // Count number of students
    @Query("SELECT COUNT(s) FROM Course c JOIN c.students s WHERE c.courseId = :courseId")
    Integer countNumberOfStudents(@Param("courseId") Long courseId);

    @Query("SELECT c FROM Course c WHERE c.teacher.userId = :teacherId")
    List<Course> findCoursesByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT c FROM Course c WHERE c.teacher.userId = :teacherId AND c.teacher.role.role = 'INSTRUCTOR'")
    List<Course> findCoursesByTeacherIdString(@Param("teacherId") Long teacherId);
}