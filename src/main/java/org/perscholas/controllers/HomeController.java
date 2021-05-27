package org.perscholas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final StudentService studentService;
    private final CourseService courseService;

    public HomeController(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }


    @ModelAttribute("student")
    public Student student() {
        return new Student();
    }

    @ModelAttribute("course")
    public Course course() {
        return new Course();
    }

    /*
            - controllers should be separated e.g. @RequestMapping("admin"), @RequestMapping("student")
            - provide as much as possible e.g. get/post/put/delete mappings
     */

    @GetMapping("/")
    public String home() {
        return "redirect:template";
    }

    @GetMapping("/template")
    public String template(){
        return "template";
    }

}
