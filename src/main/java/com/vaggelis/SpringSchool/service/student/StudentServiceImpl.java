package com.vaggelis.SpringSchool.service.student;

import com.vaggelis.SpringSchool.dto.request.PatchRequest;
import com.vaggelis.SpringSchool.dto.request.SignUpRequest;
import com.vaggelis.SpringSchool.dto.request.UpdateRequest;
import com.vaggelis.SpringSchool.exception.student.StudentAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.student.StudentNotFoundException;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.*;
import com.vaggelis.SpringSchool.repository.ICourseRepository;
import com.vaggelis.SpringSchool.repository.IStudentRepository;
import com.vaggelis.SpringSchool.repository.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements IStudentService{

    private final IUserRepository userRepository;
    private final IStudentRepository studentRepository;
    private final ICourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    /**Creates a Student and user with STUDENT role
     *
     * @param request
     * @return
     * @throws StudentAlreadyExistsException
     */
    @Override
    public Student studentSignUp(SignUpRequest request) throws StudentAlreadyExistsException {
        User user;
        Student student;

        try {
            //Maps student to the signup request
            student = Mapper.extractStudentFromSignUpRequest(request);
            //Maps the user to the signup request
            user = Mapper.extractUserWithStudentRoleFromSignUpRequest(request, passwordEncoder);

            //Checks if the user already exists
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

    /**Finds a student by the id
     *
     * @param id
     * @return
     * @throws StudentNotFoundException
     */
    @Override
    public Student findStudentById(Long id) throws StudentNotFoundException {
        Student student;

        try {
            //Find the student by the id
            student = studentRepository.findStudentById(id);
            if (student == null) throw new StudentNotFoundException(Student.class, student.getId());
        }catch (StudentNotFoundException e){
            throw e;
        }
        return student;
    }

    /**Finds all students
     *
     * @return
     */
    @Override
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    /**Deletes a student by its id
     *
     * @param id
     * @return
     * @throws StudentNotFoundException
     */
    @Override
    public Student deleteStudent(Long id) throws StudentNotFoundException {
        Student student;

        try {
            //Find the student by the id and if it exists it gets deleted
            student = studentRepository.findStudentById(id);
            if (student == null) throw new StudentNotFoundException(Student.class, student.getId());
            studentRepository.delete(student);
        }catch (StudentNotFoundException e){
            throw e;
        }
        return student;
    }

    /**Shows your student's user profile
     *
     * @param targetId
     * @return
     * @throws StudentNotFoundException
     */
    @Override
    public Student seeYourProfile(Long targetId) throws StudentNotFoundException {
        // Get the logged-in user's email from the JWT authentication context
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Student targetStudent = null;

        try {
            // Find the logged-in user
            User currentUser = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Make sure the logged-in user is actually a student
            targetStudent = studentRepository.findById(targetId)
                    .orElseThrow(() -> new StudentNotFoundException(Student.class, targetId));

            if (!targetStudent.getUser().getId().equals(currentUser.getId())) {
                throw new SecurityException("You are not authorized to see this profile");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (StudentNotFoundException e) {
            throw new RuntimeException(e);
        }

        return targetStudent;
    }

    /**Patches the logged student not it's user
     *
     * @param request
     * @return
     * @throws StudentNotFoundException
     */
    @Override
    public Student patchYourStudent(PatchRequest request) throws StudentNotFoundException {
        // Get the logged-in user's email from the JWT authentication context
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Student targetStudent;

        try {
            // Find the logged-in user
            User currentUser = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Find the student
            targetStudent = studentRepository.findById(request.getId())
                    .orElseThrow(() -> new StudentNotFoundException(Student.class, request.getId()));
            //Checks if exists
            if (!targetStudent.getUser().getId().equals(currentUser.getId())){
                throw new SecurityException("You are not authorized to see this profile");
            }

            // Use the mapper to update only student fields
            Mapper.patchStudentFromRequest(targetStudent, request);
        } catch (StudentNotFoundException e) {
            throw  e;
        }
        return studentRepository.save(targetStudent);
    }

    /**Updates both the user and the student
     *
     * @param request
     * @return
     * @throws StudentNotFoundException
     */
    @Override
    public Student updateStudentAndUser(UpdateRequest request) throws StudentNotFoundException {
        User user;
        Student student;

        try {
            //Finds the student by the id
            student = studentRepository.findStudentById(request.getId());
            if (student == null) throw new StudentNotFoundException(Student.class, request.getId());

            //Gets the student's user
            user = student.getUser();

            //Maps the user to the update request
            Mapper.updateUserFromRequest(user, request, passwordEncoder);
                //Maps the student to the update request
            Mapper.updateStudentFromRequest(student, request);


            studentRepository.save(student);
        }catch (StudentNotFoundException e){
            throw e;
        }
        return student;
    }


    /**Adds a course to a student
     *
     * @param studentId
     * @param courseId
     * @return
     * @throws EntityNotFoundException
     */
    @Override
    public Student addCourseToStudent(Long studentId, Long courseId) throws EntityNotFoundException {
        Student student;
        Course course;

        try {
            //Finds the student
            student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new EntityNotFoundException("Student not found"));

            //Finds the course
            course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new EntityNotFoundException("Course not found"));


            student.addCourse(course);
            studentRepository.save(student);
        }catch (EntityNotFoundException e){
            throw e;
        }
        return student;
    }

    /**Removes a course from a student
     *
     * @param studentId
     * @param courseId
     * @return
     * @throws EntityNotFoundException
     */
    @Override
    public Student removeCourseFromStudent(Long studentId, Long courseId) throws EntityNotFoundException {
        Student student;
        Course course;

        try {
            //Finds the Student
            student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new EntityNotFoundException("Student not found"));
            //Finds the course
            course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new EntityNotFoundException("Course not found"));


            student.removeCourse(course);
            studentRepository.save(student);

            //Finds if course has any students and if not it gets deleted
            List<Student> students = studentsOfCourse(course);
            if (students.isEmpty()) {courseRepository.delete(course);}
        }catch (EntityNotFoundException e){
            throw e;
        }
        return student;
    }


    /**Finds all the courses a student has
     *
     * @param id
     * @return
     * @throws StudentNotFoundException
     */
    @Override
    public List<Course> findAllStudentsCourses(Long id) throws StudentNotFoundException {
        return courseRepository.findAllCoursesByStudentId(id);
    }

    /**Finds all the students a course has
     *
     * @param course
     * @return
     */
    private List<Student> studentsOfCourse(Course course){
        return  course.getStudents().stream().toList();
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
