package com.peklo.peklo.models.task_3;

import com.peklo.peklo.exceptions.UrlNotConnection;
import com.peklo.peklo.exceptions.UrlNotCorrect;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = {
        "com.peklo.peklo.models.task_3",
})
public class ControllerHandlerTask3 {

    private final String redirectTask_3Result = "redirect:result";

    @ExceptionHandler(UrlNotConnection.class)
    public String urlNotConnection(){
        return String.format("%s?%s", redirectTask_3Result, "error=connection");
    }

    @ExceptionHandler(UrlNotCorrect.class)
    public String urlNotCorrect(){
        return String.format("%s?%s", redirectTask_3Result, "error=not-correct");
    }
}
