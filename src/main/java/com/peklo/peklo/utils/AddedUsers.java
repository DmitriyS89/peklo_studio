package com.peklo.peklo.utils;

import com.peklo.peklo.models.User.User;
import com.peklo.peklo.models.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AddedUsers {

    private final PasswordEncoder encoder;

    @Bean
    CommandLineRunner run(UserRepository userRepository){
        return (args) -> {
            int size = userRepository.findAll().size();
            if(size < 2){
                User person = User.builder()
                        .username("test")
                        .password(encoder.encode("1234"))
                        .email("test@gmail.com")
                        .role(Roles.ADMIN)
                        .active(true)
                        .build();

                User person2 = User.builder()
                        .username("test2")
                        .password(encoder.encode("1234"))
                        .email("test2@gmail.com")
                        .role(Roles.USER)
                        .active(true)
                        .build();

                userRepository.save(person);
                userRepository.save(person2);
            }
        };
    }
}
