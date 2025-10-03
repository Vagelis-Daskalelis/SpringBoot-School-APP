package com.vaggelis.SpringSchool.service;

import com.vaggelis.SpringSchool.dto.JWTAuthenticationResponse;
import com.vaggelis.SpringSchool.dto.SignInRequest;
import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.dto.UpdateRequest;
import com.vaggelis.SpringSchool.exception.teacher.TeacherAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.teacher.TeacherNotFoundException;
import com.vaggelis.SpringSchool.exception.user.UserNotFoundException;
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
    private final PasswordEncoder passwordEncoder;
    private final IJWTService ijwtService;
    private final AuthenticationManager authenticationManager;




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
    public User changeStatus(Long id) throws UserNotFoundException {
        User user;

        try {
            user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(User.class, id));

            if (user.getStatus().equals(Status.ACTIVE)){
                user.setStatus(Status.BANNED);
            }else {
                user.setStatus(Status.ACTIVE);
            }

            userRepository.save(user);
        }catch (UserNotFoundException e){
            throw e;
        }
        return user;
    }
}

