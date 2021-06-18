package com.peklo.peklo.models.telegram_bot;

import com.peklo.peklo.models.User.User;
import com.peklo.peklo.models.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("telegram-bot")
@RequiredArgsConstructor
public class TelegramBotController {

    private final TelegramTokenService telegramTokenService;
    private final UserService userService;

    @GetMapping("accept-telegram-account")
    public String getToken(@RequestParam(defaultValue = "null") String tokenAccept, Principal principal, Model model){
        User user = userService.findByEmail(principal.getName());
        String token = telegramTokenService.findTgToken(user.getId());
        model.addAttribute("userId", user.getId());
        model.addAttribute("userToken", token);
        model.addAttribute("tokenAccept", tokenAccept);
        return "telegram-bot-accept-token";
    }

    @PostMapping("new-token-for-user")
    public String newToken(@RequestParam Long userId){
        String newToken = telegramTokenService.newToken();
        telegramTokenService.saveToken(userId, newToken);
        return "redirect:/telegram-bot/accept-telegram-account";
    }
}
