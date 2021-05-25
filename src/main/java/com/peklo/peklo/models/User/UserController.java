package com.peklo.peklo.models.User;

import com.peklo.peklo.utils.Roles;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class UserController {

    private final UserService userService;

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

        if (!userService.addUser(UserWithPasswordDto.fromDTO(user))) {
            model.addAttribute("usernameError", "User exists!");
            return "register";
        }


        return "redirect:/login";
    }

    static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }

    @GetMapping("/identify")
    public String identify() {
        return "forgotPassword";
    }
}