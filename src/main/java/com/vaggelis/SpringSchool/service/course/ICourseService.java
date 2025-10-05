package com.vaggelis.SpringSchool.service.course;

import com.vaggelis.SpringSchool.dto.course.CourseInsertDTO;
import com.vaggelis.SpringSchool.dto.course.CourseUpdateDTO;
import com.vaggelis.SpringSchool.exception.course.CourseNotFoundException;
import com.vaggelis.SpringSchool.models.Course;

import java.util.List;

public interface ICourseService {
    Course addCourse(CourseInsertDTO dto);
    Course findCourseById(Long id) throws CourseNotFoundException;
    List<Course> findAll();
    Course updateCourse(CourseUpdateDTO dto) throws CourseNotFoundException;
    Course deleteCourse(Long id) throws CourseNotFoundException;
}
