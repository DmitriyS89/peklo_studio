package com.peklo.peklo.models.token;

import com.peklo.peklo.exceptions.TokenNotAccepted;
import com.peklo.peklo.exceptions.TokenNotFound;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = {
        "com.peklo.peklo.models.User",
        "com.peklo.peklo.models.token",
})
public class TokenControllerHAndler {

    @ExceptionHandler(TokenNotFound.class)
    private String showErrorNotFound(){
        return "redirect:activation?accept=error";
    }

    @ExceptionHandler(TokenNotAccepted.class)
    private String showErrorNotAccepted(TokenNotAccepted ex){
        return "redirect:identify?accept=error&userEmail=" + ex.getMessage();
    }
}
