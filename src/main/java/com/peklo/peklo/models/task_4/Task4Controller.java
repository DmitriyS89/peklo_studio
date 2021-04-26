package com.peklo.peklo.models.task_4;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "tool_4")
public class Task4Controller {
    private final Task4Service task4Service;

    @GetMapping("/run")
    public String runTask4(@RequestParam String urlFromFront, @RequestParam String protocol, Model model){
        String baseUrl = task4Service.getBaseUrl(urlFromFront,protocol);
        List<Results4Task> results = task4Service.results(baseUrl);
        model.addAttribute("results",results);
        return "redirect:tool_4/result";
    }

    @GetMapping("result")
    public String tool4(){
        return "tool_4";
    }
}
