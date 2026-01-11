package com.bca6th.project.motorbikebackend.service;

import com.bca6th.project.motorbikebackend.model.Role;

public interface UserService {
    void changeRole(Long id, Role newRole);
}
