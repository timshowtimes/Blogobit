package com.timinsquare.blogobit2.repositories;

import com.timinsquare.blogobit2.models.BUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BUserRepo extends JpaRepository<BUser, Integer> {
    Optional<BUser> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
