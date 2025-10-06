package com.vaggelis.SpringSchool.mapper;

import com.vaggelis.SpringSchool.dto.course.CourseInsertDTO;
import com.vaggelis.SpringSchool.dto.course.CourseReadDTO;
import com.vaggelis.SpringSchool.dto.course.CourseUpdateDTO;
import com.vaggelis.SpringSchool.dto.image.ImageReadDTO;
import com.vaggelis.SpringSchool.dto.request.PatchRequest;
import com.vaggelis.SpringSchool.dto.request.SignUpRequest;
import com.vaggelis.SpringSchool.dto.request.UpdateRequest;
import com.vaggelis.SpringSchool.dto.speciality.SpecialityInsertDTO;
import com.vaggelis.SpringSchool.dto.speciality.SpecialityReadDTO;
import com.vaggelis.SpringSchool.dto.speciality.SpecialityUpdateDTO;
import com.vaggelis.SpringSchool.dto.student.StudentReadDTO;
import com.vaggelis.SpringSchool.dto.teacher.TeacherReadDTO;
import com.vaggelis.SpringSchool.dto.user.UserReadDTO;
import com.vaggelis.SpringSchool.models.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


public class Mapper {


private Mapper() {} // prevent instantiation


    // ===========================
    // Teacher
    // ===========================


    //Creates a Teacher from the signUp request
    public static Teacher extractTeacherFromSignUpRequest(SignUpRequest request) {
        return new Teacher(null, request.getFirstname(), request.getLastname());
    }


    //Maps Teacher to TeacherReadDto
    public static TeacherReadDTO mappingTeacherToReadDto(Teacher teacher){
        if (teacher.getSpeciality() == null) {
            return new TeacherReadDTO(teacher.getId(), teacher.getFirstname(), teacher.getLastname(), null, Mapper.mappingUserToReadDto(teacher.getUser()));
        } else{
            return new TeacherReadDTO(teacher.getId(), teacher.getFirstname(), teacher.getLastname(), Mapper.mapReadDtoSpeciality(teacher.getSpeciality()), Mapper.mappingUserToReadDto(teacher.getUser()));}
    }

    //Maps Patch request to the teacher
    public static void patchTeacherFromRequest(Teacher teacher, PatchRequest request) {
        teacher.setFirstname(request.getFirstname());
        teacher.setLastname(request.getLastname());
    }

    //Maps Update request to the teacher
    public static void updateTeacherFromRequest(Teacher teacher, UpdateRequest request){
        teacher.setFirstname(request.getFirstname());
        teacher.setLastname(request.getLastname());
    }


    // ===========================
    // Student
    // ===========================


    //Creates a Student from the signUp request
    public static Student extractStudentFromSignUpRequest(SignUpRequest request) {
        return new Student(null, request.getFirstname(), request.getLastname());
    }


    // Maps Student to StudentReadDto
    public static StudentReadDTO mappingStudentToReadDto(Student student) {
        List<CourseReadDTO> courseDTOs = null;

        if (student.getCourses() != null && !student.getCourses().isEmpty()) {
            courseDTOs = student.getCourses().stream()
                    .map(Mapper::mapReadDtoTOCourse) // convert each Course to CourseReadDTO
                    .toList();
        }

        return new StudentReadDTO(
                student.getId(),
                student.getFirstname(),
                student.getLastname(),
                Mapper.mappingUserToReadDto(student.getUser()),
                courseDTOs
        );
    }


    //Maps Patch request to the student
    public static void patchStudentFromRequest(Student student, PatchRequest request) {
        student.setFirstname(request.getFirstname());
        student.setLastname(request.getLastname());
    }

    //Maps Update request to the teacher
    public static void updateStudentFromRequest(Student student, UpdateRequest request){
        student.setFirstname(request.getFirstname());
        student.setLastname(request.getLastname());
    }


    // ===========================
    // User
    // ===========================


    //Creates a User with Teacher Role from the signUp request
    public static User extractUserWithTeacherRoleFromSignUpRequest(SignUpRequest request, PasswordEncoder passwordEncoder) {
        return User.getNewUserWithTeacherRole(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail()
        );
    }


    //Creates a User with Student Role from the signUp request
    public static User extractUserWithStudentRoleFromSignUpRequest(SignUpRequest request, PasswordEncoder passwordEncoder) {
        return User.getNewUserWithStudentRole(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail()
        );
    }

    //Maps User to UserReadDto
    public static UserReadDTO mappingUserToReadDto(User user) {
        UserReadDTO dto = new UserReadDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());

        if (user.getImage() != null) {
            Image img = user.getImage();
            ImageReadDTO imgDto = new ImageReadDTO(
                    img.getId(),
                    img.getFileName(),
                    img.getFileType(),
                    "/images/download/" + img.getId() // build URL here
            );
            dto.setImage(imgDto);
        }

        return dto;
    }

    //Maps Update request to the user
    public static void updateUserFromRequest(User user, UpdateRequest request, PasswordEncoder passwordEncoder){
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
    }


    // ===========================
    // Image
    // ===========================


    //Maps image to ImageReadDTO
    public static Image mappingImageDtoToImage(ImageReadDTO dto){
        return new Image(dto.getId(), dto.getFileName(), dto.getDownloadUrl());
    }


    // ===========================
    // Speciality
    // ===========================

    // Creates a speciality from SpecialityInsertDTO
    public static Speciality mapSpecialityToInsertDto(SpecialityInsertDTO dto){
        return new Speciality(null, dto.getName());
    }


    //Maps SpecialityUpdateDTO  to the speciality
    public static void mapSpecialityToUpdateDto(Speciality speciality, SpecialityUpdateDTO dto){
        speciality.setId(dto.getId());
        speciality.setName(dto.getName());
    }

    //Maps speciality to SpecialityReadDTO
    public static SpecialityReadDTO mapReadDtoSpeciality(Speciality speciality){
        return new SpecialityReadDTO(speciality.getId(), speciality.getName());
    }


    // ===========================
    // Course
    // ===========================

    // Creates a course from CourseInsertDTO
    public static Course mapCourseToInsertDto(CourseInsertDTO dto){
        return new Course(null, dto.getName(), dto.getDate(), dto.getHours());
    }

    //Maps CourseUpdateDTO  to the course
    public static void mapCourseToUpdateDto(Course course, CourseUpdateDTO dto){
        course.setName(dto.getName());
        course.setDate(dto.getDate());
        course.setHours(dto.getHours());
    }

    //Maps Course to CourseReadDTO
    public static CourseReadDTO mapReadDtoTOCourse(Course course){
        return new CourseReadDTO(course.getId(), course.getName(), course.getDate(), course.getHours());
    }
}
