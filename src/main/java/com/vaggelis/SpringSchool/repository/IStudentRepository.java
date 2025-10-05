package com.vaggelis.SpringSchool.repository;

import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IStudentRepository extends JpaRepository<Student, Long> {
    Student findStudentByLastname(String lastname);
    Student findStudentById(Long id);

    Optional<Student> findByUser(User currentUser);
}
