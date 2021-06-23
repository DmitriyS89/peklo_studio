package com.peklo.peklo.models.task_4;

import com.peklo.peklo.exceptions.UrlNotConnection;
import com.peklo.peklo.exceptions.UrlNotCorrect;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.MalformedURLException;

@ControllerAdvice(basePackages = {
        "com.peklo.peklo.models.task_4",
})
public class ControllerHandlerTask4 {

    private final String redirectTask_4Result = "redirect:result";

    @ExceptionHandler(UrlNotConnection.class)
    public String urlNotConnection(){
        return String.format("%s?%s", redirectTask_4Result, "error=connection");
    }

    @ExceptionHandler({UrlNotCorrect.class, MalformedURLException.class})
    public String urlNotCorrect(){
        return String.format("%s?%s", redirectTask_4Result, "error=not-correct");
    }
}
