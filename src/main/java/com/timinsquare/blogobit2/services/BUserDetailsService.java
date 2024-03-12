package com.timinsquare.blogobit2.services;

import com.timinsquare.blogobit2.models.BUser;
import com.timinsquare.blogobit2.repositories.BUserRepo;
import com.timinsquare.blogobit2.util.CredentialsAlreadyExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BUserDetailsService implements UserDetailsService {
    private final BUserRepo userRepo;

    @Autowired
    public BUserDetailsService(BUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    public BUser create(BUser user) throws CredentialsAlreadyExist {
        if (userRepo.existsByUsername(user.getUsername()) || userRepo.existsByEmail(user.getEmail())) {
            throw new CredentialsAlreadyExist("User with this username or email is already exist");
        }
        return save(user);
    }

    @Transactional
    public BUser save(BUser user) {
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<BUser> user = userRepo.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.get();
    }

}
