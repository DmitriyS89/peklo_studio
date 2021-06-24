package com.peklo.peklo.models.task_2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "tool_2")
public class Task2_Controller {

    @GetMapping
    public String getTemplate(){
        return "notYet";
    }

}
