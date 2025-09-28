package com.vaggelis.SpringSchool.service;

import com.vaggelis.SpringSchool.dto.JWTAuthenticationResponse;
import com.vaggelis.SpringSchool.dto.SignInRequest;
import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.exception.*;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.*;
import com.vaggelis.SpringSchool.repository.IStudentRepository;
import com.vaggelis.SpringSchool.repository.ITeacherRepository;
import com.vaggelis.SpringSchool.repository.IUserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CRUDServiceImpl implements ICRUDService {

    private final IUserRepository userRepository;
    private final ITeacherRepository teacherRepository;
    private final IStudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final IJWTService ijwtService;
    private final AuthenticationManager authenticationManager;

    //Creates a Student
    @Override
    public Student studentSignUp(SignUpRequest request) throws StudentAlreadyExistsException {
        User user;
        Student student;

        try {
            student = Mapper.extractStudentFromSignUpRequest(request);
            user = Mapper.extractUserWithStudentRoleFromSignUpRequest(request, passwordEncoder);

            Optional<User> returnedUser = userRepository.findByEmail(request.getEmail());

            if (returnedUser.isPresent()) throw new StudentAlreadyExistsException(Student.class, request.getEmail());

            student.addUser(user);
            studentRepository.save(student);

            log.info("Student user added: {}", user.getEmail());
            log.info("Student added: {}, {}" , student.getFirstname(), student.getLastname());
        }catch (StudentAlreadyExistsException e){
            log.error(e.getMessage());
            throw e;
        }
        return student;
    }

    //Creates a Teacher
    @Override
    public Teacher teacherSignUp(SignUpRequest request) throws TeacherAlreadyExistsException {
        User user;
        Teacher teacher;

        try {
            teacher = Mapper.extractTeacherFromSignUpRequest(request);
            user = Mapper.extractUserWithTeacherRoleFromSignUpRequest(request, passwordEncoder);

            Optional<User> returnedUser = userRepository.findByEmail(request.getEmail());

            if (returnedUser.isPresent()) throw new TeacherAlreadyExistsException(Teacher.class, request.getEmail());

            teacher.addUser(user);
            teacherRepository.save(teacher);

            log.info("Teacher user added: {}", user.getEmail());
            log.info("Teacher added: {} , {}", teacher.getFirstname(), teacher.getLastname());
        }catch (TeacherAlreadyExistsException e){
            log.error(e.getMessage());
            throw e;
        }

        return teacher;
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


    //Signs in
    @Override
    public JWTAuthenticationResponse SignIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        var jwt = ijwtService.generateToken(user);
        log.info("User logged in {} with email: " , request.getEmail());
        return  JWTAuthenticationResponse.builder().token(jwt).build();
    }



    //Logs out
    @Override
    public void logout(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()){
            User user = userOpt.get();
            log.info("User logged out: {}", email);
        }else {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
    }

    //Creates a admin when the application is starting
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




    //Finds User by email
    @Override
    public User findUserByEmail(String email) throws UserNotFoundException {
        User user;

        try {
            user = userRepository.findUserByEmail(email);
            if (user == null) throw new UserNotFoundException(User.class, user.getId());
        }catch (UserNotFoundException e){
            throw e;
        }
        return user;
    }

    //Finds all Users
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Teacher findTeacherById(Long id) throws TeacherNotFoundException {
        Teacher teacher;

        try {
            teacher = teacherRepository.findTeacherById(id);
            if (teacher == null) throw new TeacherNotFoundException(Teacher.class, teacher.getId());
        }catch (TeacherNotFoundException e){
            throw e;
        }
        return teacher;
    }

    //Finds all Teachers
    @Override
    public List<Teacher> findAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Student findStudentById(Long id) throws StudentNotFoundException {
        Student student;

        try {
            student = studentRepository.findStudentById(id);
            if (student == null) throw new StudentNotFoundException(Student.class, student.getId());
        }catch (StudentNotFoundException e){
            throw e;
        }
        return student;
    }

    //Finds all Students
    @Override
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    //Deletes a teacher by its id
    @Override
    public Teacher deleteTeacher(Long id) throws TeacherNotFoundException {
        Teacher teacher;

        try {
            teacher = teacherRepository.findTeacherById(id);
            if (teacher == null) throw new TeacherNotFoundException(Teacher.class, teacher.getId());
            teacherRepository.delete(teacher);
        }catch (TeacherNotFoundException e){
            throw e;
        }
        return teacher;
    }

    //Deletes a student by its id
    @Override
    public Student deleteStudent(Long id) throws StudentNotFoundException {
        Student student;

        try {
            student = studentRepository.findStudentById(id);
            if (student == null) throw new StudentNotFoundException(Student.class, student.getId());
            studentRepository.delete(student);
        }catch (StudentNotFoundException e){
            throw e;
        }
        return student;
    }



    //Find the students profile that matches his id
    @Override
    public Student seeYourProfile(Long targetId) throws StudentNotFoundException {
        // Get the logged-in user's email from the JWT authentication context
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        // Find the logged-in user
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Make sure the logged-in user is actually a student
        Student targetStudent = studentRepository.findById(targetId)
                .orElseThrow(() -> new StudentNotFoundException(Student.class, targetId));

        if (!targetStudent.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not authorized to see this profile");
        }

        return targetStudent;
    }



    //Even simpler (no targetId)
    //
    //If the endpoint is only for the logged-in student, you can drop targetId completely:
//    @Override
//    public Student seeYourProfile() throws StudentNotFoundException {
//        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        User currentUser = userRepository.findByEmail(currentUserEmail)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        return studentRepository.findByUser(currentUser)
//                .orElseThrow(() -> new StudentNotFoundException(Student.class, currentUser.getId()));
//    }
}


/**
 *     // updates the user that you are logged in
 *     @Override
 *     public User updateYourUser(UpdateRequest request, Long targetUserId, Long currentUserId) throws UserNotFoundException {
 *         if (!targetUserId.equals(currentUserId)){
 *             throw new SecurityException("You are not authorized to update this user.");
 *         }
 *         User targetUser = null;
 *         try {
 *             targetUser = userRepository.findById(targetUserId)
 *                     .orElseThrow(() -> new UserNotFoundException(User.class, targetUserId));
 *
 *             targetUser.setUname(request.getUname());
 *             targetUser.setEmail(request.getEmail());
 *             targetUser.setPassword(passwordEncoder.encode(request.getPassword()));
 *
 *             userRepository.save(targetUser);
 *         } catch (UserNotFoundException e) {
 *             throw e;
 *         }
 *
 *         return targetUser;
 *     }
 *
 *     // deletes the user that you are logged in
 *     @Override
 *     public User deleteYourUser(Long targetUserId, Long currentUserId) throws UserNotFoundException {
 *         if (!targetUserId.equals(currentUserId)){
 *             throw new SecurityException("You are not authorized to update this user.");
 *         }
 *         try {
 *             User targetUser = userRepository.findById(targetUserId)
 *                     .orElseThrow(() -> new UserNotFoundException(User.class, targetUserId));
 *
 *             userRepository.delete(targetUser);
 *             return targetUser;
 *         } catch (UserNotFoundException e) {
 *             throw e;
 *         }
 *
 *     }
 */
