package org.perscholas.services;

import org.springframework.transaction.annotation.Transactional;

public class StudentService {

    /*
            - add class annotations
            - add @Transactional on class or on each method
            - add crud methods
     */

    public List<Student> getAllStudent() {
        return studentRepo.findAll();
    }

    public Student saveStudent(Student s) {
        return studentRepo.save(s);
    }

    public Student getStudentByEmail(String email) {
        return studentRepo.getById(email);
    }

    public boolean validateStudent(Student student) {
        // If the optional is present, then the email and password match.
        return studentRepo.findStudentByEmailAndPassword(student.getEmail(), student.getPassword()).isPresent();
    }

    public void registerStudentToCourse(String email, long courseId) {
        Optional<Student> studentOptional = studentRepo.findStudentByEmailWithCourses(email);
//        if(studentOptional.isPresent()) {
//            studentOptional.
//            Student student = studentOptional.get();
//
//        }
    }

}
