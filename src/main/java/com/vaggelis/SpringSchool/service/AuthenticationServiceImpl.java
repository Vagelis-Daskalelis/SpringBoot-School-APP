package com.vaggelis.SpringSchool.service;

import com.vaggelis.SpringSchool.dto.JWTAuthenticationResponse;
import com.vaggelis.SpringSchool.dto.SignInRequest;
import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.exception.StudentAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.TeacherAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.UserAlreadyExistsException;
import com.vaggelis.SpringSchool.mapper.UserMapper;
import com.vaggelis.SpringSchool.models.*;
import com.vaggelis.SpringSchool.repository.IStudentRepository;
import com.vaggelis.SpringSchool.repository.ITeacherRepository;
import com.vaggelis.SpringSchool.repository.IUserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements IAuthenticationService{

    private final IUserRepository userRepository;
    private final ITeacherRepository teacherRepository;
    private final IStudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final IJWTService ijwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public void studentSignUp(SignUpRequest request) throws StudentAlreadyExistsException {
        User user;
        Student student;

        try {
            student = UserMapper.extractStudentFromSignUpRequest(request);
            user = UserMapper.extractUserWithStudentRoleFromSignUpRequest(request, passwordEncoder);

            Optional<User> returnedUser = userRepository.findByEmail(request.getEmail());

            if (returnedUser.isPresent()) throw new StudentAlreadyExistsException(Student.class, request.getEmail());

            student.addUser(user);
            studentRepository.save(student);

            log.info("Student added");
        }catch (StudentAlreadyExistsException e){
            log.error(e.getMessage());
            throw e;
        }
        //return student;
    }

    @Override
    public JWTAuthenticationResponse SignIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        var jwt = ijwtService.generateToken(user);
        log.info("User logged in" , request.getEmail());
        return  JWTAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public void teacherSignUp(SignUpRequest request) throws TeacherAlreadyExistsException {
        User user;
        Teacher teacher;

        try {
            teacher = UserMapper.extractTeacherFromSignUpRequest(request);
            user = UserMapper.extractUserWithTeacherRoleFromSignUpRequest(request, passwordEncoder);

            Optional<User> returnedUser = userRepository.findByEmail(request.getEmail());

            if (returnedUser.isPresent()) throw new TeacherAlreadyExistsException(Teacher.class, request.getEmail());

            teacher.addUser(user);
            teacherRepository.save(teacher);

            log.info("Teacher added");
        }catch (TeacherAlreadyExistsException e){
            log.error(e.getMessage());
            throw e;
        }

        //return teacher;
    }


//    @Override
//    public void teacherSignUp(SignUpRequest request) throws TeacherAlreadyExistsException {
//        // Check if user with email already exists
//        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
//            throw new TeacherAlreadyExistsException(Teacher.class, request.getEmail());
//        }
//
//        // Create User with teacher role
//        User user = UserMapper.extractUserWithTeacherRoleFromSignUpRequest(request, passwordEncoder);
//
//        // Create Teacher and associate User
//        Teacher teacher = UserMapper.extractTeacherFromSignUpRequest(request);
//        teacher.addUser(user);
//
//        // Save Teacher (user will be saved automatically due to CascadeType.ALL)
//        try {
//            teacherRepository.save(teacher);
//            log.info("Teacher added successfully: {}", teacher.getFirstname() + " " + teacher.getLastname());
//        } catch (Exception e) {
//            log.error("Error saving teacher: {}", e.getMessage());
//            throw new RuntimeException("Failed to save teacher", e);
//        }
//    }

    @Override
    public void logout(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()){
            User user = userOpt.get();
            log.info("User logged out", email);
        }else {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
    }

    @PostConstruct
    public void createAdmin(){
        Optional<User> admin = userRepository.findByRole(Role.ADMIN);
        if (admin.isEmpty()){
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setEmail("admin@gmail.com");
            user.setRole(Role.ADMIN);
            user.setStatus(Status.ACTIVE);

            Teacher teacher = new Teacher();
            teacher.setFirstname("admin");
            teacher.setLastname("admin");

           teacher.addUser(user);

           teacherRepository.save(teacher);

            System.out.println("Admin created successfully");
            log.info("Admin created successfully");
        }else {
            System.out.println("Admin already exists");
            log.info("Admin already exists");
        }
    }
}
