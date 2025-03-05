package com.github.gradehub.repositories;

import com.github.gradehub.entities.Role;
import com.github.gradehub.entities.RoleType;
import com.github.gradehub.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT u.personFirstName, u.personLastName, u.role FROM Users u WHERE u.role.role = :role")
    List<Object[]> findUserByRole(@Param("role") RoleType role);

}
