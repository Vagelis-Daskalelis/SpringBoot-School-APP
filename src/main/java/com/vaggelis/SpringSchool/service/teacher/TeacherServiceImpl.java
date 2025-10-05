package com.vaggelis.SpringSchool.service.teacher;

import com.vaggelis.SpringSchool.dto.request.PatchRequest;
import com.vaggelis.SpringSchool.dto.request.SignUpRequest;
import com.vaggelis.SpringSchool.dto.request.UpdateRequest;
import com.vaggelis.SpringSchool.exception.teacher.TeacherAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.teacher.TeacherNotFoundException;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Speciality;
import com.vaggelis.SpringSchool.models.Student;
import com.vaggelis.SpringSchool.models.Teacher;
import com.vaggelis.SpringSchool.models.User;
import com.vaggelis.SpringSchool.repository.ISpecialityRepository;
import com.vaggelis.SpringSchool.repository.ITeacherRepository;
import com.vaggelis.SpringSchool.repository.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final ISpecialityRepository specialityRepository;
    private final PasswordEncoder passwordEncoder;


    /**Creates a Student and user with STUDENT role
     *
     * @param request
     * @return
     * @throws TeacherAlreadyExistsException
     */
    @Override
    public Teacher teacherSignUp(SignUpRequest request) throws TeacherAlreadyExistsException {
        User user;
        Teacher teacher;

        try {
            //Maps student to the signup request
            teacher = Mapper.extractTeacherFromSignUpRequest(request);
            //Maps user to the signup request
            user = Mapper.extractUserWithTeacherRoleFromSignUpRequest(request, passwordEncoder);

            //Checks if the user already exists
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

    /**Finds teacher by the id
     *
     * @param id
     * @return
     * @throws TeacherNotFoundException
     */
    @Override
    public Teacher findTeacherById(Long id) throws TeacherNotFoundException {
        Teacher teacher;

        try {
            //Find the student by the id
            teacher = teacherRepository.findTeacherById(id);
            if (teacher == null) throw new TeacherNotFoundException(Teacher.class, teacher.getId());
        }catch (TeacherNotFoundException e){
            throw e;
        }
        return teacher;
    }


    /**Finds all teachers
     *
     * @return
     */
    @Override
    public List<Teacher> findAllTeachers() {
        return teacherRepository.findAll();
    }

    /**Deletes a teacher by its id
     *
     * @param id
     * @return
     * @throws TeacherNotFoundException
     */
    @Override
    public Teacher deleteTeacher(Long id) throws TeacherNotFoundException {
        Teacher teacher;

        try {
            //Find the teacher by the id and if it exists it gets deleted
            teacher = teacherRepository.findTeacherById(id);
            if (teacher == null) throw new TeacherNotFoundException(Teacher.class, teacher.getId());
            teacherRepository.delete(teacher);
        }catch (TeacherNotFoundException e){
            throw e;
        }
        return teacher;
    }

    /**Updates both the user and the teacher
     *
     * @param request
     * @return
     * @throws TeacherNotFoundException
     */
    @Override
    public Teacher updateTeacherAndUser(UpdateRequest request)throws TeacherNotFoundException {
        User user;
        Teacher teacher;

        try {
            //Finds the teacher by the id
            teacher = teacherRepository.findTeacherById(request.getId());
            if (teacher == null) throw new TeacherNotFoundException(Student.class, request.getId());

            //Gets the teacher's user
            user = teacher.getUser();

            //Maps the user to the update request
            Mapper.updateUserFromRequest(user, request, passwordEncoder);
            //Maps the student to the update request
            Mapper.updateTeacherFromRequest(teacher, request);


            teacherRepository.save(teacher);
        }catch (TeacherNotFoundException e){
            throw e;
        }
        return teacher;
    }

    /**Patches the logged teacher not it's user
     *
     * @param request
     * @return
     * @throws TeacherNotFoundException
     */
    @Override
    public Teacher patchYourTeacher(PatchRequest request) throws TeacherNotFoundException {
        // Get the logged-in user's email from the JWT authentication context
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Teacher targetTeacher;

        try {
            // Find the logged-in user
            User currentUser = userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Find the teacher
            targetTeacher = teacherRepository.findById(request.getId())
                    .orElseThrow(() -> new TeacherNotFoundException(Teacher.class, request.getId()));

            //Checks if exists
            if (!targetTeacher.getUser().getId().equals(currentUser.getId())){
                throw new SecurityException("You are not authorized to see this profile");
            }

            // Use the mapper to update only teacher fields
            Mapper.patchTeacherFromRequest(targetTeacher, request);
        } catch (TeacherNotFoundException e) {
            throw  e;
        }
        return teacherRepository.save(targetTeacher);
        }

    /**Adds a course to a student
     *
     * Each time it is used the old speciality gets replaced by the new one
     * @param teacherId
     * @param specialityId
     * @return
     * @throws EntityNotFoundException
     */
    @Override
    public Teacher addSpecialityToTeacher(Long teacherId, Long specialityId) throws EntityNotFoundException {
        Teacher teacher;
        Speciality speciality;

        try {
            //Finds the teacher
            teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

            //Finds the speciality
            speciality = specialityRepository.findById(specialityId)
                    .orElseThrow(() -> new EntityNotFoundException("Speciality not found"));

            //if (teacher.getSpeciality() != null)

            teacher.addSpeciality(speciality);
            teacherRepository.save(teacher);
        }catch (EntityNotFoundException e){
            throw e;
        }
        return teacher;
    }

    /**Remove course from a student
     *
     * @param id
     * @return
     * @throws TeacherNotFoundException
     */
    @Override
    public Teacher removeSpecialityFromTeacher(Long id) throws TeacherNotFoundException {
        Teacher teacher;
        Speciality speciality;

        try {
            //Finds the teacher
            teacher = teacherRepository.findById(id)
                    .orElseThrow(() -> new TeacherNotFoundException(Teacher.class, id));
            //Get his speciality and remove it
            speciality = teacher.getSpeciality();

            teacher.removeSpeciality(speciality);
            teacherRepository.save(teacher);
        }catch (TeacherNotFoundException e){
            throw e;
        }
        return teacher;
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
