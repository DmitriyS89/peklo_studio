package com.peklo.peklo.models.User;

import com.peklo.peklo.exceptions.TokenNotAccepted;
import com.peklo.peklo.exceptions.TokenNotFound;
import com.peklo.peklo.models.token.Token;
import com.peklo.peklo.models.token.TokenService;
import com.peklo.peklo.utils.Roles;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
@Validated
@Controller
@AllArgsConstructor
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false, defaultValue = "false") Boolean error, Model model) {
        model.addAttribute("error", error);
        return "login";
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        var user = userService.findByEmail(principal.getName());
        model.addAttribute("user", UserDto.from(user));
        if (user.getRole().equals(Roles.ADMIN)) {
            model.addAttribute("admin", "admin");
        }
        return "profile";
    }


    @GetMapping("/singup")
    public String singUp() {
        return "register";
    }

    @PostMapping("/singup")
    public String signUp(@Valid UserWithPasswordDto user, BindingResult bindingResult, Model model) {
        boolean isConfirmEmpty = StringUtils.isEmpty(user.getPassword2());

        if(isConfirmEmpty){
            model.addAttribute("password2Error", "Password confirmation cannot is empty");
        }
        if (user.getPassword() != null && !user.getPassword().equals(user.getPassword2())) {
            model.addAttribute("passwordError", "Passwords are different!");
        }

        if (isConfirmEmpty || bindingResult.hasErrors()) {
            Map<String, String> errors = getErrors(bindingResult);

            model.mergeAttributes(errors);
            return "register";
        }

        User dto = UserWithPasswordDto.fromDTO(user);
        if (!userService.addUser(dto)) {
            model.addAttribute("usernameError", "User exists!");
            return "register";
        }

        Token token = tokenService.saveToken(dto, tokenService.makeToken());
        userService.sendMessage(dto, token.getToken());
        return "redirect:activation";
    }

    static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }

    @PostMapping("/identify")
    public String identifyPost(@RequestParam String userEmail) {
        User user = userService.findByEmail(userEmail);
        Token token = tokenService.saveToken(user, tokenService.makeToken());
        //userService.sendMessage(dto, token.getToken());
        return "redirect:identify?userEmail=" + user.getEmail();
    }

    @GetMapping("/identify")
    public String identify(@RequestParam(defaultValue = "null") String accept, @RequestParam String userEmail, Model model) {
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("accept", accept);
        return "forgotPassword";
    }

    @GetMapping("/activation")
    public String confirmRegister(@RequestParam(defaultValue = "null") String accept, Model model) {
        model.addAttribute("accept", accept);
        return "confirmRegister";
    }

    @PostMapping("/activation")
    public String confirmRegister(@RequestParam Integer token) {
        if (tokenService.findToken(token)) {
            Token userToken = tokenService.getToken(token);
            userService.changeUserActivate(userToken);
            tokenService.deleteToken(userToken);
            return "redirect:login";
        } else
            throw new TokenNotFound();
    }

    @GetMapping("/forgot")
    public String forgotPassword(@RequestParam(defaultValue = "null") String accept, Model model) {
        model.addAttribute("accept", accept);
        return "forgotPasswordMail";
    }

    @PostMapping("/newPassword")
    public String changePassword(@RequestParam @NotBlank String password, @RequestParam String userEmail){
        User user = userService.findByEmail(userEmail);
        userService.changeUserPassword(user, password);
        return "redirect:login";
    }

    @GetMapping("/resetPassword")
    public String resetPassword(@RequestParam String userEmail, @RequestParam Integer token,  Model model){
        if (tokenService.findToken(token)) {
            Token userToken = tokenService.getToken(token);
            tokenService.deleteToken(userToken);
            model.addAttribute("userEmail", userEmail);
            return "newPassword";
        } else
            throw new TokenNotAccepted(userEmail);
    }
}