package com.bca6th.project.motorbikebackend.service;

import com.bca6th.project.motorbikebackend.model.Role;
import com.bca6th.project.motorbikebackend.model.User;
import com.bca6th.project.motorbikebackend.repository.UserRepository;
import com.bca6th.project.motorbikebackend.security.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.net.Authenticator;

public class UserServiceImpl implements UserService{

    public  UserRepository userRepository;

    @Override
    public void changeRole(Long id, Role newRole) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
//        Prevent changing own role or demoting SUPERADMIN
//        Authenticator auth = SecurityContextHolder.getContext().getAuthentication();
//        if(id.equals(((UserDetailsServiceImpl) auth.getPrincipal()).getId())){
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to change your role");
//        }


        user.setRole(newRole);
        userRepository.save(user);
    }
}
