package com.bca6th.project.motorbikebackend.controller;

import com.bca6th.project.motorbikebackend.dto.user.RoleChangeRequest;
import com.bca6th.project.motorbikebackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("{id}/role")
    @PreAuthorize("hasRole('SUPERADMIN')") // Only SUPERADMIN can change roles
    public ResponseEntity<Void> changeRole(@PathVariable Long id, @RequestBody RoleChangeRequest dto){
        userService.changeRole(id, dto.getRole());
        return ResponseEntity.ok().build();
    }
}
