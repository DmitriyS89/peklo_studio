package com.peklo.peklo.models.task_4;

import com.peklo.peklo.exceptions.UrlNotConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "tool_4")
public class Task4Controller {
    private final Task4Service task4Service;

    @PostMapping("/run")
    public String runTask4(@RequestParam String urlFromFront, @RequestParam String protocol, Model model) throws UrlNotConnection {
        String baseUrl = task4Service.getBaseUrl(urlFromFront, protocol);
        List<Results4Task> results = task4Service.results(baseUrl);
        model.addAttribute("results", results);
        model.addAttribute("error", "ok");
        return "tool_4";
    }

    @GetMapping("result")
    public String tool4(@RequestParam(name = "error", required = false, defaultValue = "NULL") String clientError,
                        Model model){
        String error = "NULL".equals(clientError) ? "null" : clientError;
        model.addAttribute("error", error);
        return "tool_4";
    }
}