package com.vaggelis.SpringSchool.service.student;

import com.vaggelis.SpringSchool.dto.PatchRequest;
import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.dto.UpdateRequest;
import com.vaggelis.SpringSchool.exception.student.StudentAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.student.StudentNotFoundException;
import com.vaggelis.SpringSchool.models.Student;

import java.util.List;

public interface IStudentService {
    Student studentSignUp(SignUpRequest request) throws StudentAlreadyExistsException;
    Student findStudentById(Long id) throws StudentNotFoundException;
    List<Student> findAllStudents();
    Student deleteStudent(Long id) throws StudentNotFoundException;
    Student seeYourProfile(Long targetId) throws StudentNotFoundException;
    Student updateStudentAndUser(UpdateRequest request) throws StudentNotFoundException;
    Student patchYourStudent(PatchRequest request) throws StudentNotFoundException;
}
