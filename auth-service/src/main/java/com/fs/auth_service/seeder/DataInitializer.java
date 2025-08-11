package com.fs.auth_service.seeder;


import com.fs.auth_service.dao.RoleRepository;
import com.fs.auth_service.dao.UserRepository;
import com.fs.auth_service.model.Role;
import com.fs.auth_service.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        var adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_ADMIN").build()));
        var userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));

        if (userRepository.findByUsername("admin").isEmpty()) {
            var admin = User.builder()
                    .username("admin")
                    .passwordHash(passwordEncoder.encode("adminpass"))
                    .build();
            admin.getRoles().add(adminRole);
            admin.getRoles().add(userRole);
            userRepository.save(admin);
        }

        if (userRepository.findByUsername("user").isEmpty()) {
            var user = User.builder()
                    .username("user")
                    .passwordHash(passwordEncoder.encode("userpass"))
                    .build();
            user.getRoles().add(userRole);
            userRepository.save(user);
        }
    }
}
