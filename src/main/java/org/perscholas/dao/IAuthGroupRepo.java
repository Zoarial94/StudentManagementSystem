package org.perscholas.dao;

import org.perscholas.models.AuthGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAuthGroupRepo extends JpaRepository<AuthGroup, Long> {
    List<AuthGroup> findByaUsername(String username);
}
