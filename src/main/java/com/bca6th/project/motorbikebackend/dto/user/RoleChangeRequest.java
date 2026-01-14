package com.bca6th.project.motorbikebackend.dto.user;

import com.bca6th.project.motorbikebackend.model.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleChangeRequest {
    private Role role;

    public Role getRole(){
        return role;
    }

    public void setRole(Role role){
        this.role = role;
    }
}
