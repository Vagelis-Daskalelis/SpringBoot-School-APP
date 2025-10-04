package com.vaggelis.SpringSchool.repository;

import com.vaggelis.SpringSchool.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICourseRepository extends JpaRepository<Course, Long> {
}
