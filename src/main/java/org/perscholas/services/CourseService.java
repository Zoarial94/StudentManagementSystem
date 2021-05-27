package org.perscholas.services;
import org.perscholas.dao.ICourseRepo;
import org.perscholas.models.Course;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;


@Service
public class CourseService  {

    private final ICourseRepo courseRepo;

    @Autowired
    public CourseService(ICourseRepo courseRepo) {
        this.courseRepo = courseRepo;
    }

     /*
            - add class annotations
            - add @Transactional on class or on each method
            - add crud methods
     */

    public Course saveCourse(Course course) {
        return courseRepo.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        Optional<Course> courseOptional = courseRepo.findCourseById(id);
        return courseOptional;
    }




}
