package org.perscholas.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.perscholas.dao.ICourseRepo;
import org.perscholas.dao.IStudentRepo;
import org.perscholas.exceptions.FileStorageException;
import org.perscholas.models.Student;
import org.perscholas.security.AppSecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log
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

    public Optional<Student> findStudentByUsername(String username) {
        return studentRepo.findByUsername(username);
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

    @Value("${app.upload.dir:${user.home}}")
    String uploadDir;

    public void addImageToStudent(MultipartFile file, String email) {


        var student = getStudentByEmail(email);
        if(student == null) {
            throw new RuntimeException("No student found.");
        }

        try {
            //File extension
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            String path = uploadDir + File.separator + "studentImages" + File.separator + email + "-image" + ext;
            // location
            Path copyLocation = Paths.get(path);
            log.info("File location: " + copyLocation.toString());
            //Replaces file if it already exists
            Files.copy(file.getInputStream(),copyLocation, StandardCopyOption.REPLACE_EXISTING);
            student.setImagePath("fileupload" + File.separator + "studentImages" + File.separator + email + "-image" + ext);
            saveStudent(student);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("Could not store file " + file.getOriginalFilename() + ". Please try again.");
        }

    }

}
