package com.vaggelis.SpringSchool.service;

import com.vaggelis.SpringSchool.dto.JWTAuthenticationResponse;
import com.vaggelis.SpringSchool.dto.SignInRequest;
import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.exception.UserAlreadyExistsException;
import com.vaggelis.SpringSchool.models.Role;
import com.vaggelis.SpringSchool.models.Status;
import com.vaggelis.SpringSchool.models.User;
import com.vaggelis.SpringSchool.repository.IUserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements IAuthenticationService{

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IJWTService ijwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public void signUp(SignUpRequest request) throws UserAlreadyExistsException {

    }

    @Override
    public JWTAuthenticationResponse SignIn(SignInRequest request) {
        return null;
    }

    @Override
    public void managerSignUp(SignUpRequest request) throws UserAlreadyExistsException {

    }

    @Override
    public void logout(String email) {

    }

    @PostConstruct
    public void createAdmin(){
        Optional<User> admin = userRepository.findByRole(Role.ADMIN);
        if (admin.isEmpty()){
            User user = new User();
            user.setFirstname("admin");
            user.setLastname("admin");
            user.setUname("admin");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setEmail("admin@gmail.com");
            user.setRole(Role.ADMIN);
            user.setStatus(Status.ACTIVE);
            userRepository.save(user);
            System.out.println("Admin created successfully");
            log.info("Admin created successfully");
        }else {
            System.out.println("Admin already exists");
            log.info("Admin already exists");
        }
    }
}
