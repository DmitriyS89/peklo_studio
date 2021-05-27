package com.peklo.peklo.models.User;

import com.peklo.peklo.exceptions.UserMailNotFound;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = {
        "com.peklo.peklo.models.User"
})
public class UserHandlerController {

    @ExceptionHandler(UserMailNotFound.class)
    private String showErrorEmailNotFound(){
        return "redirect:forgot?accept=error";
    }
}
