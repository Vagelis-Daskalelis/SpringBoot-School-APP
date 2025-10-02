package com.vaggelis.SpringSchool.mapper;

import com.vaggelis.SpringSchool.dto.*;
import com.vaggelis.SpringSchool.models.*;
import org.springframework.security.crypto.password.PasswordEncoder;


public class Mapper {


private Mapper() {} // prevent instantiation

    //Creates a Teacher from the signUp request
    public static Teacher extractTeacherFromSignUpRequest(SignUpRequest request) {
        return new Teacher(null, request.getFirstname(), request.getLastname());
    }

    //Creates a User with Teacher Role from the signUp request
    public static User extractUserWithTeacherRoleFromSignUpRequest(SignUpRequest request, PasswordEncoder passwordEncoder) {
        return User.getNewUserWithTeacherRole(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail()
        );
    }

    //Creates a Student from the signUp request
    public static Student extractStudentFromSignUpRequest(SignUpRequest request) {
        return new Student(null, request.getFirstname(), request.getLastname());
    }

    //Creates a User with Student Role from the signUp request
    public static User extractUserWithStudentRoleFromSignUpRequest(SignUpRequest request, PasswordEncoder passwordEncoder) {
        return User.getNewUserWithStudentRole(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail()
        );
    }


//        //Maps User to UserReadDto
//        public static UserReadDTO mappingUserToReadDto(User user){
//            return new UserReadDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getRole(), user.getStatus(), user.getImage());
//        }

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
            dto.setImageReadDTO(imgDto);
        }

        return dto;
    }

        //Maps Teacher to TeacherReadDto
        public static TeacherReadDTO mappingTeacherToReadDto(Teacher teacher){
            return new TeacherReadDTO(teacher.getId(), teacher.getFirstname(), teacher.getLastname(), Mapper.mappingUserToReadDto(teacher.getUser()));
        }

    //Maps Student to StudentReadDto
    public static StudentReadDTO mappingStudentToReadDto(Student student){
        return new StudentReadDTO(student.getId(), student.getFirstname(), student.getLastname(), Mapper.mappingUserToReadDto(student.getUser()));
    }

    //Map to update a Student
    public static Student extractStudentFromUpdateRequest(UpdateRequest request, User user){
        return new Student(request.getId(), request.getFirstname(), request.getLastname(), user);
    }

    // Update only student fields
    public static void patchStudentFromRequest(Student student, PatchRequest request) {
        student.setFirstname(request.getFirstname());
        student.setLastname(request.getLastname());
        // Don't touch student.getUser() at all
    }

    public static void patchTeacherFromRequest(Teacher teacher, PatchRequest request) {
        teacher.setFirstname(request.getFirstname());
        teacher.setLastname(request.getLastname());
        // Don't touch student.getUser() at all
    }

    public static void updateStudentFromRequest(Student student, UpdateRequest request){
        student.setFirstname(request.getFirstname());
        student.setLastname(request.getLastname());
    }

    public static void updateTeacherFromRequest(Teacher teacher, UpdateRequest request){
        teacher.setFirstname(request.getFirstname());
        teacher.setLastname(request.getLastname());
    }

    public static void updateUserFromRequest(User user, UpdateRequest request, PasswordEncoder passwordEncoder){
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
    }

    public static Image mappingImageDtoToImage(ImageReadDTO dto){
        return new Image(dto.getId(), dto.getFileName(), dto.getDownloadUrl());
    }

}
