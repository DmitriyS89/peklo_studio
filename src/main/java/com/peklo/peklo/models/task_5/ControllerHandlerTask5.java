package com.peklo.peklo.models.task_5;

import com.peklo.peklo.exceptions.ConnectionNotFound;
import com.peklo.peklo.exceptions.UrlNotCorrect;
import com.peklo.peklo.exceptions.UserMailNotFound;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = {
        "com.peklo.peklo.models.task_5",
})
public class ControllerHandlerTask5 {

    private final String redirectTask_3Result = "redirect:result";

    @ExceptionHandler(UserMailNotFound.class)
    public String userNotFound(){
        return String.format("%s?%s", redirectTask_3Result, "success=userIsNull");
    }

    @ExceptionHandler(ConnectionNotFound.class)
    public String groupNotFound(){
        return String.format("%s?%s", redirectTask_3Result, "success=groupIsNull");
    }

    @ExceptionHandler(UrlNotCorrect.class)
    public String urlNotCorrect(){
        return String.format("%s?%s", redirectTask_3Result, "success=urlNotCorrect");
    }
}
