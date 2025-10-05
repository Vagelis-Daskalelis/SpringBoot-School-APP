package com.vaggelis.SpringSchool.service.course;

import com.vaggelis.SpringSchool.dto.course.CourseInsertDTO;
import com.vaggelis.SpringSchool.dto.course.CourseUpdateDTO;
import com.vaggelis.SpringSchool.exception.course.CourseNotFoundException;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Course;
import com.vaggelis.SpringSchool.repository.ICourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService{

    private final ICourseRepository courseRepository;

    /**Adds a course
     *
     * @param dto
     * @return
     */
    @Override
    public Course addCourse(CourseInsertDTO dto) {
        Course course;

        //Maps CourseInsertDTO to course
        course = Mapper.mapCourseToInsertDto(dto);

        courseRepository.save(course);

        return course;
    }

    /**Find course by the id
     *
     * @param id
     * @return
     * @throws CourseNotFoundException
     */
    @Override
    public Course findCourseById(Long id) throws CourseNotFoundException {
        Course course;

        try {
            //Finds course by id
            course = courseRepository.findCourseById(id);
            //If the course exists it gets returned
            if (course == null) throw new CourseNotFoundException(Course.class, id);
        }catch (CourseNotFoundException e){
            throw e;
        }

        return course;
    }

    /**Find all courses
     *
     * @return
     */
    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    /**Updates a course
     *
     * @param dto
     * @return
     * @throws CourseNotFoundException
     */
    @Override
    public Course updateCourse(CourseUpdateDTO dto) throws CourseNotFoundException {
        Course course;

        try {
            //finds the course by the id and if it exists it gets updated
            course = courseRepository.findCourseById(dto.getId());
            if (course == null) throw new CourseNotFoundException(Course.class, dto.getId());

            //Maps the course to the courseUpdatedto
            Mapper.mapCourseToUpdateDto(course, dto);
            courseRepository.save(course);
        }catch (CourseNotFoundException e){
            throw e;
        }
        return course;
    }

    /**Deletes course
     *
     * @param id
     * @return
     * @throws CourseNotFoundException
     */
    @Override
    public Course deleteCourse(Long id) throws CourseNotFoundException {
        Course course;

        try {
            //Finds course by id
            course = courseRepository.findCourseById(id);
            //If the course exists it gets deleted
            if (course == null) throw new CourseNotFoundException(Course.class, id);
            courseRepository.delete(course);
        } catch (CourseNotFoundException e) {
            throw e;
        }

        return course;
    }

}

