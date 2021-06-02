package org.perscholas.security;

import org.perscholas.dao.IAuthGroupRepo;
import org.perscholas.models.Student;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.perscholas.dao.IStudentRepo;
import java.util.Optional;
import org.perscholas.models.AuthGroup;
import java.util.List;


@Service
public class AppUserDetailsService implements UserDetailsService {

    //Fields
    private final IStudentRepo studentRepo;
    private final IAuthGroupRepo authGroup;

    //Constructor
    public AppUserDetailsService(IStudentRepo studentRepo, IAuthGroupRepo authGroup) {
        this.studentRepo = studentRepo;
        this.authGroup = authGroup;
    }

    //Load user by username. If the user doesn't exist, throw exception saying user can't be found.
    //Return an AppUserPrincipal for the specific user being loaded.
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Optional<Student> studentOptional = studentRepo.findByUsername(s);

        if(studentOptional.isEmpty()) {
            throw new UsernameNotFoundException("Cannot find " + s);
        }

        List<AuthGroup> authGroups = authGroup.findByaUsername(s);
        return new AppUserPrincipal(studentOptional.get(), authGroups);
    }

}
