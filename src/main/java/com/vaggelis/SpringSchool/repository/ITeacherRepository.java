package com.vaggelis.SpringSchool.repository;

import com.vaggelis.SpringSchool.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findTeacherByLastname(String lastname);
    Teacher findTeacherById(Long id);
}
