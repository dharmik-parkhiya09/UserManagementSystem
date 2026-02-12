package org.dharmik.usermanegmentapp.Repositary;

import org.dharmik.usermanegmentapp.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {

    Optional<Role> findByName(String name);
}
