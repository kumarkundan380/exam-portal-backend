package com.exam.repository;

import com.exam.enums.UserRole;
import com.exam.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(UserRole userRole);

    Set<Role> findByRoleNameIn(List<UserRole> userRoles);

}
