package org.perscholas.controllers;


import lombok.RequiredArgsConstructor;
import org.perscholas.models.Course;
import org.perscholas.models.Student;
import org.perscholas.services.CourseService;
import org.perscholas.services.StudentService;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Controller
public class HomeController {

    private final StudentService studentService;
    private final CourseService courseService;

    //Student Controllers
    @ModelAttribute("student")
    public Student student() {
        return new Student();
    }

    // Course controllers
    @ModelAttribute("course")
    public Course course() {
        return new Course();
    }

    /*
            - controllers should be separated e.g. @RequestMapping("admin"), @RequestMapping("student")
            - provide as much as possible e.g. get/post/put/delete mappings
     */

    //Accessible to everyone. Redirects to Template
    @GetMapping("/")
    public String home() {
        return "redirect:template";
    }

    //Accessible to everyone. Directs to Template
    @GetMapping("/template")
    public String template(){
        return "template";
    }

    //Accessible to everyone. The first page seen if not logged in.
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //Seen if page is not accessible to user.
    @GetMapping("/403")
    public String forbidden() {
        var user = SecurityContextHolder.getContext().getAuthentication();
        StringBuilder auths = new StringBuilder();
        for(var auth : user.getAuthorities()) {
            auths.append(auth.getAuthority()).append(", ");
        }
        log.error("**** " + user.getName() + " Attempted to Access Forbidden Page with authority " + auths + "****");
        return "403";
    }

    @GetMapping("/student")
    public String studentHome(Model model, Principal principal) {
        model.addAttribute("student", studentService.findStudentByUsername(principal.getName()).get());
        return "studentHome";
    }

    //Accessible to DEAN and ADMIN Roles. READ Permission only.
    @PreAuthorize("hasAnyRole('DEAN', 'ADMIN') and hasAuthority('READ')")
    @GetMapping("/allStudents")
    public String allStudents(Model model) {
        model.addAttribute("allStudents", studentService.getAllStudent());
        return "allStudents";
    }

    //Accessible to everyone. READ Permission only.
    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/allCourses")
    public String allCourses(Model model) {
        model.addAttribute("allCourses", courseService.getAllCourses());
        return "allCourses";
    }

    /*
     *  Create a student.
     *  (This includes the GET and POST function)
     */
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('WRITE')")
    @GetMapping("/student/register")
    public String studentRegistration(){
        return "studentRegistration";
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('WRITE')")
    @PostMapping("/student/register")
    public String studentRegister(@ModelAttribute("student") @Valid Student student, BindingResult result, Model model) {
        System.out.println(result.hasErrors());
        if(result.hasErrors()) {
            return "studentRegistration";

        }else{
            Student databaseStudent = studentService.saveStudent(student);
            model.addAttribute("student", student);
            return "studentConfirmation";
        }
    }

    /*
     *  Create a course
     *  (This includes the GET and POST function)
     */
    @PreAuthorize("hasRole('Admin') and hasAuthority('WRITE')")
    @GetMapping("/course/register")
    public String courseRegistration() {
        return "courseRegistration";
    }

    @PreAuthorize("hasRole('Admin') and hasAuthority('WRITE')")
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


    /*
     *
     */
    @GetMapping("/course/registerStudent")
    public String courseStudentRegistration(@SessionAttribute("student") Student student, Model model){

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
