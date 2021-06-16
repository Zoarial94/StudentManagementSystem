package org.perscholas.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

//lombok
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
//database
@Entity
//springboot
@Component
@Slf4j
public class Student implements Serializable {
    static final long serialVersionUID = 6381462249347345007L;





    /*
            note use annotation  '@ToString.Exclude' for Lists
            ----------------
            - add validation for fields
            - relationships
            - utilities methods if any
     */

    //fields
    @Id
    @Email(regexp = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b",message = "Invalid email address")
    String email;

    @Length(min = 3, max = 25, message = "should be between {1} and {2}")
    @NotBlank(message = "Please Enter a Username")
    String username;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "- at least 8 characters\n- must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number\n- Can contain special characters")
    String password;
    
    String imagePath;

    // Create a table where each student can only but assigned to the course once.
    @ManyToMany
    @JoinTable(
            uniqueConstraints = {@UniqueConstraint(columnNames = {"Student_email", "courses_id"})}
    )
    List<Course> courses;

    public Student(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return email.equals(student.email) && Objects.equals(username, student.username) && Objects.equals(password, student.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, username, password);
    }
}
