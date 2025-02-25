package com.github.gradehub.repositories;

import com.github.gradehub.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    // Find users by email
    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Optional<Users> findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);

    // Find users by name and last name
    @Query("SELECT u FROM Users u WHERE u.personFirstName = :person_first_name AND u.personLastName = :person_last_name")
    Optional<Users> findByNameAndLastName(@Param("person_first_name") String name, @Param("person_last_name") String lastName);

    // Find users for a specific course with pagination
    @Query("SELECT u FROM Users u JOIN u.courses c WHERE c.courseId = :course_id")
    Page<Users> findUsersByCourseId(@Param("course_id") Long courseId, Pageable pageable);

    // Overloaded method without pagination (for use in tests)
    @Query("SELECT u FROM Users u JOIN u.courses c WHERE c.courseId = :course_id")
    List<Users> findUsersByCourseId(@Param("course_id") Long courseId);}
