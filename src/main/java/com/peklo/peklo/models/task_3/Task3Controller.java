package com.peklo.peklo.models.task_3;

import com.peklo.peklo.exceptions.UrlNotConnection;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "tool_3")
public class Task3Controller {
    private final Task3Service task3Service;

    @GetMapping("/run")
    public String runTask3(@RequestParam String url, Model model) throws UrlNotConnection {
        Document baseUrl = task3Service.getJSoupConnection(url);
        List<Task3Element> results = task3Service.run(String.valueOf(baseUrl));
        model.addAttribute("result_tool_3",results);
        return "redirect:tool_3/result";
    }

    @GetMapping("result")
    public String tool3(){
        return "tool_3";
    }
}
