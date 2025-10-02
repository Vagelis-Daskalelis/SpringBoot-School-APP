package com.vaggelis.SpringSchool.service.teacher;

import com.vaggelis.SpringSchool.dto.PatchRequest;
import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.dto.UpdateRequest;
import com.vaggelis.SpringSchool.exception.student.StudentNotFoundException;
import com.vaggelis.SpringSchool.exception.teacher.TeacherAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.teacher.TeacherNotFoundException;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.models.Teacher;
import com.vaggelis.SpringSchool.models.User;
import com.vaggelis.SpringSchool.repository.ITeacherRepository;
import com.vaggelis.SpringSchool.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherServiceImpl implements ITeacherService{

    private final IUserRepository userRepository;
    private final ITeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public Teacher updateTeacherAndUser(UpdateRequest request)throws TeacherNotFoundException {
        User user;
        Teacher teacher;

        try {
            teacher = teacherRepository.findTeacherById(request.getId());
            if (teacher == null) throw new TeacherNotFoundException(Student.class, request.getId());

            user = teacher.getUser();

            Mapper.updateUserFromRequest(user, request, passwordEncoder);
            Mapper.updateTeacherFromRequest(teacher, request);


            teacherRepository.save(teacher);
        }catch (TeacherNotFoundException e){
            throw e;
        }
        return teacher;
    }

    @Override
    public Teacher patchYourTeacher(PatchRequest request) throws TeacherNotFoundException {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Teacher targetTeacher;

        try {
            User currentUser = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            targetTeacher = teacherRepository.findById(request.getId())
                    .orElseThrow(() -> new TeacherNotFoundException(Teacher.class, request.getId()));

            if (!targetTeacher.getUser().getId().equals(currentUser.getId())){
                throw new SecurityException("You are not authorized to see this profile");
            }

            // Use the mapper to update only student fields
            Mapper.patchTeacherFromRequest(targetTeacher, request);
        } catch (TeacherNotFoundException e) {
            throw  e;
        }
        return teacherRepository.save(targetTeacher);
        }
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
