package org.perscholas.services;



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
}
