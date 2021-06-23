package com.peklo.peklo.models.task_3;

import com.peklo.peklo.exceptions.UrlNotConnection;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "tool_3")
public class Task3Controller {
    private final Task3ServiceImpl task3ServiceImpl;

    @PostMapping("run")
    public String runTask3(@RequestParam String url, Model model) throws UrlNotConnection {
        Document document = task3ServiceImpl.getJSoupConnection(url);
        SiteWithDomain siteWith3rdDomain = task3ServiceImpl.makeSiteEntity(url);
        List<Task3Element> results = task3ServiceImpl.filterPatterns(task3ServiceImpl.getLinks(document), siteWith3rdDomain);
        List<Task3Element> filter = task3ServiceImpl.filter(results);
        model.addAttribute("results", filter);
        model.addAttribute("error", "ok");
        model.addAttribute("fromUrl", url);
        task3ServiceImpl.makeExcel(filter, url);
        return "tool_3";
    }

    @GetMapping("run")
    public String redirectToTool_3(){
        return "redirect:http://localhost:8080/tool_3/result";
    }

    @GetMapping("result")
    public String tool3(@RequestParam(name = "error", required = false, defaultValue = "NULL") String clientError,
                        Model model){
        String error = "NULL".equals(clientError) ? "null" : clientError;
        model.addAttribute("error", error);
        return "tool_3";
    }
}
