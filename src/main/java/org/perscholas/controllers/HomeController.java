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

        return "courseRegistration";
    }


    @PostMapping("/course/registerStudent")
    public String courseRegisterStudent(
            @ModelAttribute("student") @Valid Student student, BindingResult result, Model model) {
        System.out.println(result.hasErrors());
        if(result.hasErrors()) {
            return "courseRegistration";

        }else{
            //Student databaseStudent = studentServices.saveStudent(student);
            //student.setCourses(student.getCourses().add(course));
            return "studentConfirmation";
        }
    }

    @GetMapping("getsession")
    public String getSession(){
    return "getsession";
    }

}
