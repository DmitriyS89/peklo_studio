package com.peklo.peklo.models.User;

import com.peklo.peklo.models.token.Token;
import com.peklo.peklo.models.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
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

    public void sendMessage(User user, Integer token) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Come here to activation: http://%s/activation\n" +
                            "Activation code: %s ",
                    user.getUsername(),
                    hostname,
                    token

            );
            mailSender.send(user.getEmail(), "Activation", message);
        }
    }

    public void changeUserActivate(Token token) {
        User user = token.getUserId();
        user.setActive(true);
        userRepository.save(user);
    }

}
