package com.vaggelis.SpringSchool.service.teacher;

import com.vaggelis.SpringSchool.dto.PatchRequest;
import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.dto.UpdateRequest;
import com.vaggelis.SpringSchool.exception.teacher.TeacherAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.teacher.TeacherNotFoundException;
import com.vaggelis.SpringSchool.models.Teacher;

import java.util.List;

public interface ITeacherService {
    Teacher teacherSignUp(SignUpRequest request) throws TeacherAlreadyExistsException;
    Teacher findTeacherById(Long id) throws TeacherNotFoundException;
    List<Teacher> findAllTeachers();
    Teacher deleteTeacher(Long id) throws TeacherNotFoundException;
    Teacher updateTeacherAndUser(UpdateRequest request)throws TeacherNotFoundException;
    Teacher patchYourTeacher(PatchRequest patchRequest)throws TeacherNotFoundException;
}
