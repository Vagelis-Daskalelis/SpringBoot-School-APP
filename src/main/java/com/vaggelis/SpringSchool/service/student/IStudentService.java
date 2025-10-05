package com.vaggelis.SpringSchool.service.student;

import com.vaggelis.SpringSchool.dto.request.PatchRequest;
import com.vaggelis.SpringSchool.dto.request.SignUpRequest;
import com.vaggelis.SpringSchool.dto.request.UpdateRequest;
import com.vaggelis.SpringSchool.exception.student.StudentAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.student.StudentNotFoundException;
import com.vaggelis.SpringSchool.models.Course;
import com.vaggelis.SpringSchool.models.Student;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface IStudentService {
    Student studentSignUp(SignUpRequest request) throws StudentAlreadyExistsException;
    Student findStudentById(Long id) throws StudentNotFoundException;
    List<Student> findAllStudents();
    Student deleteStudent(Long id) throws StudentNotFoundException;
    Student seeYourProfile(Long targetId) throws StudentNotFoundException;
    Student updateStudentAndUser(UpdateRequest request) throws StudentNotFoundException;
    Student patchYourStudent(PatchRequest request) throws StudentNotFoundException;
    Student addCourseToStudent(Long StudentId, Long courseId)throws EntityNotFoundException;
    Student removeCourseFromStudent(Long StudentId, Long courseId)throws EntityNotFoundException;
    List<Course> findAllStudentsCourses(Long id)throws StudentNotFoundException;
}
