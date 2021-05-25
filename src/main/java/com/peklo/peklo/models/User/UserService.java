package com.peklo.peklo.models.User;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MailSender mailSender;

    @Value("${hostname}")
    private String hostname;


    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public boolean addUser(User user) {
        Optional<User> userFromDb = userRepository.findByEmail(user.getUsername());
        if (!userFromDb.isEmpty()) { return false; }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Come here to activation: http://%s/activation",
                    user.getUsername(),
                    hostname
            );
            mailSender.send(user.getEmail(), "Activation", message);
        }
    }
}
