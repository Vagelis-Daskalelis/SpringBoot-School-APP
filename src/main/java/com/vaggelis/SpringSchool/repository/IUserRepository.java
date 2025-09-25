package com.vaggelis.SpringSchool.repository;

import com.vaggelis.SpringSchool.models.Role;
import com.vaggelis.SpringSchool.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByRole(Role role);

    User findUserByEmail(String email);
    User findUserById(Long id);
}
