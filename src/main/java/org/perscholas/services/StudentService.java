package org.perscholas.services;

import lombok.RequiredArgsConstructor;
import org.perscholas.dao.ICourseRepo;
import org.perscholas.dao.IStudentRepo;
import org.perscholas.models.Student;
import org.perscholas.security.AppSecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StudentService {

    private final IStudentRepo studentRepo;
    private final ICourseRepo courseRepo;
    private final PasswordEncoder passwordEncoder;

/*
            - add class annotations
            - add @Transactional on class or on each method
            - add crud methods
     */

    public List<Student> getAllStudent() {
        return studentRepo.findAll();
    }

    public Student saveStudent(Student s) {
        s.setPassword(passwordEncoder.encode(s.getPassword()));
        return studentRepo.save(s);
    }

    public Student getStudentByEmail(String email) {
        return studentRepo.getById(email);
    }

    public Optional<Student> getStudentByEmailWithCourses(String email) {
        return studentRepo.findStudentByEmailWithCourses(email);
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
