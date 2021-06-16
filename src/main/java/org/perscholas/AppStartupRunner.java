package org.perscholas;

import lombok.extern.slf4j.Slf4j;
import org.perscholas.dao.IAuthGroupRepo;
import org.perscholas.dao.ICourseRepo;
import org.perscholas.dao.IStudentRepo;
import org.perscholas.models.AuthGroup;
import org.perscholas.models.Course;
import org.perscholas.models.Student;
import org.perscholas.security.AppSecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Slf4j
@Transactional
public class AppStartupRunner implements CommandLineRunner {
    ICourseRepo courseRepo;
    IStudentRepo studentRepo;
    IAuthGroupRepo authGroupRepo;

    @Autowired
    public AppStartupRunner(IStudentRepo studentRepo, ICourseRepo courseRepo, IAuthGroupRepo authGroupRepo) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
        this.authGroupRepo = authGroupRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("**** Start adding Students sql statements ****");
        var pwEncoder = AppSecurityConfiguration.getPasswordEncoder();

        studentRepo.save(new Student("habboubi", "habboubi@gmail.com", pwEncoder.encode("password")));
        studentRepo.save(new Student("admin", "admin@gmail.com", pwEncoder.encode("password")));
        studentRepo.save(new Student("student_one", "student_one@gmail.com", pwEncoder.encode("password")));
        studentRepo.save(new Student("student_two", "student_two@gmail.com", pwEncoder.encode("password")));
        studentRepo.save(new Student("student_three", "student_three@gmail.com", pwEncoder.encode("password")));
        studentRepo.save(new Student("dean", "dean@example.com", pwEncoder.encode("password")));
        log.info("**** End of Student sql statements ****");

        log.info("**** Start adding Courses sql statements ****");
        courseRepo.save(new Course("Java", 3));
        courseRepo.save(new Course("Database", 4));
        courseRepo.save(new Course("Spring Boot", 5));
        courseRepo.save(new Course("HTML & CSS", 6));
        courseRepo.save(new Course("JavaScript", 7));
        log.info("**** End of Courses sql statements ****");

        log.info("**** Start adding AuthGroup sql statements ****");
        authGroupRepo.save(new AuthGroup("habboubi", "ROLE_ADMIN"));
        authGroupRepo.save(new AuthGroup("habboubi", "ROLE_USER"));
        authGroupRepo.save(new AuthGroup("habboubi", "WRITE"));
        authGroupRepo.save(new AuthGroup("habboubi", "READ"));

        authGroupRepo.save(new AuthGroup("admin", "ROLE_ADMIN"));
        authGroupRepo.save(new AuthGroup("admin", "ROLE_USER"));
        authGroupRepo.save(new AuthGroup("admin", "WRITE"));
        authGroupRepo.save(new AuthGroup("admin", "READ"));

        authGroupRepo.save(new AuthGroup("student_one", "ROLE_STUDENT"));
        authGroupRepo.save(new AuthGroup("student_two", "ROLE_STUDENT"));
        authGroupRepo.save(new AuthGroup("student_three", "ROLE_STUDENT"));
        authGroupRepo.save(new AuthGroup("student_one", "READ"));
        authGroupRepo.save(new AuthGroup("student_two", "READ"));
        authGroupRepo.save(new AuthGroup("student_three", "READ"));

        authGroupRepo.save(new AuthGroup("dean", "ROLE_DEAN"));
        authGroupRepo.save(new AuthGroup("dean", "READ"));
        log.info("**** End of AuthGroup sql statements ****");

    }
}
