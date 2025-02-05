package ntp.springboot3.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ntp.springboot3.dto.request.response.UserResponse;
import ntp.springboot3.entity.User;
import ntp.springboot3.enums.Role;
import ntp.springboot3.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepo userRepo) {
        return args -> {
              if (userRepo.findByUsername("admin").isEmpty()){
                  var roles = new HashSet<String>();
                  roles.add(Role.ADMIN.name());

                  User user = User.builder()
                          .username("admin")
                          .password(passwordEncoder.encode("admin"))
//                          .roles(roles)
                          .build();
                  userRepo.save(user);
                  log.warn("admin user has been created with default password: admin, please change it");
              }
        };
    }
}
