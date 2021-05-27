package org.perscholas.controllers;


import org.perscholas.models.Course;
import org.perscholas.models.Student;
import org.perscholas.services.CourseService;
import org.perscholas.services.StudentService;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@SessionAttributes({"student"})
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

    @GetMapping("/allStudents")
    public String allStudents(Model model) {
        model.addAttribute("allStudents", studentService.getAllStudent());
        return "allStudents";
    }

    @GetMapping("/allCourses")
    public String allCourses(Model model) {
        model.addAttribute("allCourses", courseService.getAllCourses());
        return "allCourses";
    }

    @GetMapping("/student")
    public String studentHome() {
        return "studentConfirmation";
    }

    @GetMapping("/student/register")
    public String studentRegistration(@SessionAttribute("student") Student student){
        return "studentRegistration";
    }

    @PostMapping("/student/register")
    public String studentRegister(@ModelAttribute("student") @Valid Student student, BindingResult result, Model model) {
        System.out.println(result.hasErrors());
        if(result.hasErrors()) {
            return "studentRegistration";

        }else{
            Student databaseStudent = studentService.saveStudent(student);
            model.addAttribute("student", student);
            return "redirect:/student";
        }
    }

    @GetMapping("/course/register")
    public String courseRegistration() {
        return "courseRegistration";
    }

    @PostMapping("/course/register")
    public String courseRegister(@ModelAttribute("course") @Valid Course course, BindingResult result, Model model) {
        System.out.println(result.hasErrors());
        if(result.hasErrors()) {
            return "courseRegistration";

        }else{
            System.out.println("Course ID: " + course.getId());
            Course newCourse = courseService.saveCourse(course);
            return "courseConfirmation";
        }
    }

    @GetMapping("/course/registerStudent")
    public String courseStudentRegistration(@SessionAttribute("student") Student student, Model model){

        if(student.getEmail() == null) {
            return "redirect:/student/login";
        }

        student = studentService.getStudentByEmail(student.getEmail());

        List<Course> allCourses = courseService.getAllCourses();
        ArrayList<Course> availableCourse = new ArrayList<>(allCourses.size());
        List<Course> studentCourses = student.getCourses();

        for(Course c : allCourses) {
            if(!studentCourses.contains(c)) {
                availableCourse.add(c);
            }
        }


        model.addAttribute("availableCourses", availableCourse);

        return "courseStudentSignup";
    }


    @PostMapping("/course/registerStudent")
    public String courseRegisterStudent(@SessionAttribute("student") Student student, @RequestParam Map<String, Long> allParams, Model model) {

        if(student.getEmail() == null) {
            return "redirect:student";
        }

        Student newKidInClass = studentService.getStudentByEmail(student.getEmail());

        for(Map.Entry<String, Long> entry : allParams.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
            newKidInClass.getCourses().add(courseService.getCourseById(entry.getValue()).get());
        }

        studentService.saveStudent(newKidInClass);

        return "redirect:student";
    }

    @PostMapping("/course/registerStudentAlt")
    public String courseRegisterStudentAlt(@RequestParam(name = "email") String email,
                                           @RequestParam(name = "courseID") long courseID)
    {

        if(courseService.getCourseById(courseID).isEmpty() || studentService.getStudentByEmail(email) == null)
        {
            return "redirect:/course/registerStudent";
        }
        Student newKidInClass = studentService.getStudentByEmail(email);

        newKidInClass.getCourses().add(courseService.getCourseById(courseID).get());

        studentService.saveStudent(newKidInClass);

        return "redirect:/student";

    }

    @GetMapping("getsession")
    public String getSession(){
    return "getsession";
    }



}
