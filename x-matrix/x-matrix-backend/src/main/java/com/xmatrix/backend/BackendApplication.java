package com.xmatrix.backend;

import com.xmatrix.backend.entity.Role;
import com.xmatrix.backend.entity.User;
import com.xmatrix.backend.mappers.RoleRepository;
import com.xmatrix.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing
public class BackendApplication {



	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		return (args) -> {
			// Initialize roles
			String[] roles = {"SolutionOwner", "GlobalOwner", "DomainOwner", "MatrixOwner"};
			for (String role : roles) {
				Role roleEntity = new Role();
				roleEntity.setName(role);
				if (roleRepository.findByName(role) == null) {
					roleRepository.save(roleEntity);
				}
			}

			// Initialize superadmin user
			User dto = User.builder()
					.username("superadmin")
					.email("superadmin@gmail.com")
					.password(passwordEncoder.encode("superadmin123"))
					.role(roleRepository.findByName("SolutionOwner"))
					.build();

			Optional<User> user = userRepository.findByEmail(dto.getEmail());
			if (user.isEmpty()) {
				userRepository.save(dto);
			}
		};
	}
}
