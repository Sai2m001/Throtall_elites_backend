package com.bca6th.project.motorbikebackend;

import com.bca6th.project.motorbikebackend.model.Role;
import com.bca6th.project.motorbikebackend.model.User;
import com.bca6th.project.motorbikebackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MotorbikebackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MotorbikebackendApplication.class, args);
	}

	@Bean
	@Transactional
	public CommandLineRunner initSuperAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if(userRepository.findByEmail("throtallelites@gmail.com").isEmpty()){
				User superAdmin = new User();
				superAdmin.setEmail("throtallelites@gmail.com");
				superAdmin.setPassword(passwordEncoder.encode("SuperAdmin123!"));
				superAdmin.setRole(Role.SUPERADMIN);
				superAdmin.setContactNo("9861603066");
				superAdmin.setName("ThrotAll_Elites_SuperAdmin");
				userRepository.save(superAdmin);
				System.out.println("superAdmin created");
			}
		};
	}
}
