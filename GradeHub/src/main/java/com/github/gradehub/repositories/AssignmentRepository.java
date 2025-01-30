package com.github.gradehub.repositories;

import com.github.gradehub.entities.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query("SELECT a FROM Assignment a WHERE a.course.courseId = :courseId")
    Page<Assignment> findAssignmentsByCourseId(@Param("courseId") Long courseId, Pageable pageable);

    @Query("SELECT a FROM Assignment a WHERE a.assignmentName LIKE %:name%")
    Page<Assignment> findAssignmentsByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT a FROM Assignment a WHERE a.dueDate > :date")
    Page<Assignment> findAssignmentsDueAfter(@Param("date") LocalDate date, Pageable pageable);

    // Count assignments by course ID
    @Query("SELECT COUNT(a) FROM Assignment a WHERE a.course.courseId = :course_id")
    Long countAssignmentsByCourseId(@Param("course_id") Long courseId);

    }
