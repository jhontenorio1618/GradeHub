package com.github.gradehub.repositories;
import com.github.gradehub.entities.Course;
import com.github.gradehub.entities.CourseGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseGradeRepository extends JpaRepository<CourseGrade, Long> {

    @Query("SELECT c FROM Course c WHERE c.teacher.usersId = :teacherId")
    List<Course> findCoursesByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.usersId = :studentId")
    List<Course> findCoursesByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(c) FROM Course c WHERE c.teacher.usersId = :teacherId")
    Long countCoursesByTeacherId(@Param("teacherId") Long teacherId);

}

