package com.vaggelis.SpringSchool.repository;

import com.vaggelis.SpringSchool.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentRepository extends JpaRepository<Student, Long> {
    Student findStudentByLastname(String lastname);
    Student findStudentById(Long id);
}
