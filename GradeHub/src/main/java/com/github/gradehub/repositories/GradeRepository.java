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
    @Query("SELECT g FROM Grade g WHERE g.user.userId = :user_id")
    Page<Grade> findByUserId(@Param("user_id") Long userId, Pageable pageable);

    // Find grades by assignment ID with pagination
    @Query("SELECT g FROM Grade g WHERE g.assignment.assignmentId = :assignment_id")
    Page<Grade> findByAssignmentId(@Param("assignment_id") Long assignmentId, Pageable pageable);

    // Find a specific grade by user ID and assignment ID
    @Query("SELECT g FROM Grade g WHERE g.user.userId = :user_id AND g.assignment.assignmentId = :assignment_id")
    Grade findByUserIdAndAssignmentId(@Param("user_id") Long userId, @Param("assignment_id") Long assignmentId);

    // Find grades below a specific score threshold with pagination
    @Query("SELECT g FROM Grade g WHERE g.score < :threshold")
    Page<Grade> findByScoreLessThan(@Param("threshold") Float threshold, Pageable pageable);

    // Find grades for a user within a specific score range with pagination
    @Query("SELECT g FROM Grade g WHERE g.user.userId = :user_id AND g.score BETWEEN :min_score AND :max_score")
    Page<Grade> findByUserIdAndScoreBetween(
            @Param("user_id") Long userId,
            @Param("min_score") Float minScore,
            @Param("max_score") Float maxScore,
            Pageable pageable
    );

    // Calculate the average grade for an assignment
    @Query("SELECT AVG(g.score) FROM Grade g WHERE g.assignment.assignmentId = :assignment_id")
    Double findAverageGradeByAssignmentId(@Param("assignment_id") Long assignmentId);

    // Find grades for an assignment below a specific score threshold with pagination
    @Query("SELECT g FROM Grade g WHERE g.assignment.assignmentId = :assignment_id AND g.score < :threshold")
    Page<Grade> findByAssignmentIdAndScoreLessThan(
            @Param("assignment_id") Long assignmentId,
            @Param("threshold") Float threshold,
            Pageable pageable
    );
}
