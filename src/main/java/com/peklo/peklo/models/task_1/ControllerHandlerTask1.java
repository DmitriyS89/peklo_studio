package com.peklo.peklo.models.task_1;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = {
        "com.peklo.peklo.models.task_1",
})
public class ControllerHandlerTask1 {

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public String notDelete(){
        return "redirect:/tool_1/client-items";
    }

}
