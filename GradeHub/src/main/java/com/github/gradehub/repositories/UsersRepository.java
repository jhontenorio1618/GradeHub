package com.github.gradehub.repositories;
import com.github.gradehub.entities.Users;
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

    // Find users by name and last name
    @Query("SELECT u FROM Users u WHERE u.personName = :name AND u.personLastName = :lastName")
    Optional<Users> findByNameAndLastName(@Param("name") String name, @Param("lastName") String lastName);

    // Find users for a specific course
    @Query("SELECT u FROM Users u JOIN u.courses c WHERE c.courseId = :courseId")
    List<Users> findUsersByCourseId(@Param("courseId") Long courseId);



}

