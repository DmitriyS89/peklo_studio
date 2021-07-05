package com.peklo.peklo.models.User;

import com.peklo.peklo.exceptions.UserMailNotFound;
import com.peklo.peklo.models.token.Token;
import com.peklo.peklo.models.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MailSender mailSender;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

    @Value("${hostname}")
    private String hostname;


    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserMailNotFound::new);
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

    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }


    public Boolean changeUserChatId(Long userId, String chatId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            user.get().setChatId(chatId);
            userRepository.save(user.get());
            return true;
        } else {
            return false;
        }
    }

    public User getUser(Long aLong) {
        return userRepository.getOne(aLong);
    }

    public void sendFile(String email, File file){
        Optional<User> userOpt = userRepository.findByEmail(email);
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            Date date = new Date(System.currentTimeMillis());
            String text = "Привет! " + simpleDateFormat.format(date);
            mailSender.sendMailWithAttachment(user.getEmail(), "Tool 1", text, file.getAbsolutePath());
        }
    }
}
