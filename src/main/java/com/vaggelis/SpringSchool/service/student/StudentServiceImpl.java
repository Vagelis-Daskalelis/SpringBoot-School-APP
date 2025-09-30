package com.vaggelis.SpringSchool.service.student;

import com.vaggelis.SpringSchool.dto.PatchRequest;
import com.vaggelis.SpringSchool.dto.SignUpRequest;
import com.vaggelis.SpringSchool.dto.UpdateRequest;
import com.vaggelis.SpringSchool.exception.student.StudentAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.student.StudentNotFoundException;
import com.vaggelis.SpringSchool.exception.teacher.TeacherNotFoundException;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.models.User;
import com.vaggelis.SpringSchool.repository.IStudentRepository;
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
public class StudentServiceImpl implements IStudentService{

    private final IUserRepository userRepository;
    private final IStudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

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

    //Finds a Student
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

    @Override
    public Student patchYourStudent(PatchRequest request) throws StudentNotFoundException {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Student targetStudent;

        try {
            User currentUser = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            targetStudent = studentRepository.findById(request.getId())
                    .orElseThrow(() -> new StudentNotFoundException(Student.class, request.getId()));

            if (!targetStudent.getUser().getId().equals(currentUser.getId())){
                throw new SecurityException("You are not authorized to see this profile");
            }

            // Use the mapper to update only student fields
            Mapper.patchStudentFromRequest(targetStudent, request);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (StudentNotFoundException e) {
            throw new RuntimeException(e);
        }

        return studentRepository.save(targetStudent);
    }

    @Override
    public Student updateStudentAndUser(UpdateRequest request) throws StudentNotFoundException {
        User updatedUser;
        Student student;
        Student updatedStudent;

        try {
            student = studentRepository.findStudentById(request.getId());
            if (student == null) throw new StudentNotFoundException(Student.class, request.getId());

            updatedUser = Mapper.extractUserFromStudentUpdateRequest(student, request, passwordEncoder);
            updatedStudent = Mapper.extractStudentFromUpdateRequest(request, updatedUser);


            studentRepository.save(updatedStudent);
        }catch (StudentNotFoundException e){
            throw e;
        }
        return updatedStudent;
    }


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
