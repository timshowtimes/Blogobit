package com.timinsquare.blogobit2.services;

import com.timinsquare.blogobit2.dto.BUserDTO;
import com.timinsquare.blogobit2.models.BUser;
import com.timinsquare.blogobit2.repositories.BUserRepo;
import com.timinsquare.blogobit2.util.CredentialsAlreadyExist;
import com.timinsquare.blogobit2.util.ErrorMessageBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BUserDetailsService implements UserDetailsService {
    private final BUserRepo userRepo;
    private final ModelMapper modelMapper;
    private ErrorMessageBuilder errorMessageBuilder;

    @Autowired
    public BUserDetailsService(BUserRepo userRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
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

    public BUser convertToBUser(BUserDTO bUserDTO) {
        return modelMapper.map(bUserDTO, BUser.class);
    }
}
