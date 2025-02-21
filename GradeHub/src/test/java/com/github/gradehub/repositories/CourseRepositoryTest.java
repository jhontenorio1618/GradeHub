package com.github.gradehub.repositories;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.github.gradehub.entities.Course;
import com.github.gradehub.entities.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;

@DataJpaTest
public class CourseRepositoryTest extends BaseTestSetup{


    @BeforeEach
    @Override
    public void setup(){
        super.setup();
    }

    @Test
    public void findCourseByStudentId() {

        List<Course> courses = courseRepository.findCoursesByStudentId(student1.getUserId());

        assertThat(courses).isNotEmpty();
        assertThat(courses).extracting(Course::getCourseName)
                .contains("COMP 324");
    }

    @Test
    public void findCoursesByTeacherId(){
        List<Course> courses = courseRepository.findCoursesByTeacherId(professor.getUserId());
        assertThat(courses).isNotEmpty();
        assertThat(courses).extracting(Course::getCourseName).contains("COMP 324");
    }

    @Test
    public void countCoursesByTeacherId(){
        List<Course> courses = courseRepository.findCoursesByTeacherId(professor.getUserId());
        assertThat(courses.size() == 1).isTrue();
    }

    @Test
    public void courseIsEmpty() {
        Course mockCourse = createCourse("COMP 300", professor, List.of());
        Integer students = courseRepository.countNumberOfStudents(mockCourse.getCourseId());
        assertThat(students).isZero();
        assertThat(courseRepository.findCoursesByTeacherId(professor.getUserId()).size() == 2).isTrue();
    }

    @Test
    public void countNumberOfStudents(){
        Long courseid = course.getCourseId();
        assertThat(courseRepository.countNumberOfStudents(courseid) == 2).isTrue();
    }



}
