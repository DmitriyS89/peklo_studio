package com.peklo.peklo.models.task_3;

import com.peklo.peklo.exceptions.UrlNotConnection;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "tool_3")
public class Task3Controller {
    private final Task3Service task3Service;

    @PostMapping("run")
    public String runTask3(@RequestParam String url, Model model) throws UrlNotConnection {
        Document baseUrl = task3Service.getJSoupConnection(url);
        List<Task3Element> results = task3Service.run(baseUrl);
        model.addAttribute("results", results);
        model.addAttribute("error", "ok");
        return "tool_3";
    }

    @GetMapping("result")
    public String tool3(@RequestParam(name = "error", required = false, defaultValue = "NULL") String clientError,
                        Model model){
        String error = "NULL".equals(clientError) ? "null" : clientError;
        model.addAttribute("error", error);
        return "tool_3";
    }
}
