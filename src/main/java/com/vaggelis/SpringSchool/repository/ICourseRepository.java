package com.vaggelis.SpringSchool.repository;

import com.vaggelis.SpringSchool.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICourseRepository extends JpaRepository<Course, Long> {
    Course findCourseById(Long id);
    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId")
    List<Course> findAllCoursesByStudentId(@Param("studentId") Long studentId);
}
