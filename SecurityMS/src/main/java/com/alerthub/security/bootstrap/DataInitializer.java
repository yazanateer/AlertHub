package com.alerthub.security.bootstrap;

import com.alerthub.security.model.RoleEntity;
import com.alerthub.security.model.UserEntity;
import com.alerthub.security.repository.RoleRepository;
import com.alerthub.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {

        if (roleRepo.count() == 0) {
            List<String> names = List.of(
                    "createAction", "updateAction", "deleteAction",
                    "createMetric", "updateMetric", "deleteMetric",
                    "triggerScan", "triggerProcess", "triggerEvaluation",
                    "read", "ADMIN"
            );

            names.forEach(name -> {
                RoleEntity role = RoleEntity.builder()
                        .name(name)
                        .build();
                roleRepo.save(role);
            });
        }

        userRepo.findByUsername("admin").orElseGet(() -> {

            var allRoles = new HashSet<>(roleRepo.findAll());

            UserEntity admin = UserEntity.builder()
                    .username("admin")
                    .email("admin@gmail.com")
                    .phone("0509999999")
                    .password(encoder.encode("admin1234"))
                    .roles(allRoles)
                    .build();

            return userRepo.save(admin);
        });
    }
}
