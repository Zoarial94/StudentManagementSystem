package org.perscholas.dao;

import org.perscholas.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/*
        - add annotation
        - extend spring jpa
        - add custom methods if needed

 */
@Repository
public interface IStudentRepo extends JpaRepository<Student, String> {
    Optional<Student> findStudentByEmailAndPassword(String email, String password);

    @Query("Select DISTINCT s FROM Student s JOIN FETCH s.courses courses WHERE s.email = ?1")
    Optional<Student> findStudentByEmailWithCourses(String email);

    Optional<Student> findByUsername(String name);
}
