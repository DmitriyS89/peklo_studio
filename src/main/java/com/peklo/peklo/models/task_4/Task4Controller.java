package com.peklo.peklo.models.task_4;

import com.peklo.peklo.exceptions.UrlNotConnection;
import com.peklo.peklo.models.User.MailSender;
import com.peklo.peklo.models.task_3.Task3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "tool_4")
public class Task4Controller {
    private final Task4Service task4Service;
    private final Task3Service task3Service;
    private final MailSender mailSender;

    @PostMapping("/run")
    public String runTask4(@RequestParam String urlFromFront, Model model, Principal principal) throws UrlNotConnection, URISyntaxException {
        List<String> urlLinks = task4Service.urlLinks(task3Service.getJSoupConnection(urlFromFront));
        List<List<Results4Task>> results4Tasks = task4Service.results(urlLinks);
        List<Results4Task> results = task4Service.setMessagesToResult(results4Tasks, urlLinks);
        task4Service.makeExcel(results, urlFromFront);
        mailSender.sendMailWithAttachment(principal.getName(), "Tool 4", "some", "result.xlsx");
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