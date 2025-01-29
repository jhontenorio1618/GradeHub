package com.github.gradehub.repositories;

import com.github.gradehub.entities.Grade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    // Find grades by user ID with pagination
    @Query("SELECT g FROM Grade g WHERE g.users.usersId = :usersId")
    Page<Grade> findByUsersId(@Param("usersId") Long usersId, Pageable pageable);

    // Find grades by assignment ID with pagination
    @Query("SELECT g FROM Grade g WHERE g.assignment.assignmentId = :assignmentId")
    Page<Grade> findByAssignmentId(@Param("assignmentId") Long assignmentId, Pageable pageable);

    // Find a specific grade by user ID and assignment ID
    @Query("SELECT g FROM Grade g WHERE g.users.usersId = :usersId AND g.assignment.assignmentId = :assignmentId")
    Grade findByUsersIdAndAssignmentId(@Param("usersId") Long usersId, @Param("assignmentId") Long assignmentId);

    // Find grades below a specific score threshold with pagination
    @Query("SELECT g FROM Grade g WHERE g.score < :threshold")
    Page<Grade> findByScoreLessThan(@Param("threshold") Float threshold, Pageable pageable);

    // Find grades for a user within a specific score range with pagination
    @Query("SELECT g FROM Grade g WHERE g.users.usersId = :usersId AND g.score BETWEEN :minScore AND :maxScore")
    Page<Grade> findByUsersIdAndScoreBetween(@Param("usersId") Long usersId, @Param("minScore") Float minScore, @Param("maxScore") Float maxScore, Pageable pageable);

    // Calculate the average grade for an assignment
    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.assignment.assignmentId = :assignmentId")
    Double findAverageGradeByAssignmentId(@Param("assignmentId") Long assignmentId);

    // Find grades for an assignment below a specific score threshold with pagination
    @Query("SELECT g FROM Grade g WHERE g.assignment.assignmentId = :assignmentId AND g.score < :threshold")
    Page<Grade> findByAssignmentIdAndScoreLessThan(@Param("assignmentId") Long assignmentId, @Param("threshold") Float threshold, Pageable pageable);
}
