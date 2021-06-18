package com.peklo.peklo.models.task_1;

import com.peklo.peklo.models.User.User;
import com.peklo.peklo.models.User.UserService;
import com.peklo.peklo.models.task_3.Task3Service;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("tool_1")
@RequiredArgsConstructor
public class ControllerTool_1 {

    private final Task1Service task1Service;
    private final Task3Service task3Service;
    private final UserService userService;

    private Boolean scripts = true;
    private String clientUrl = "";
    private Document document = null;

    @ResponseBody
    @GetMapping("getSiteElements")
    public Html getHtml(){
        return task1Service.drawSite(clientUrl, scripts);
    }

    @GetMapping()
    public String getTemplate(Principal principal){
        User user = userService.findByEmail(principal.getName());
        if(user.getChatId() == null || user.getChatId().isBlank()){
            return "redirect:/telegram-bot/accept-telegram-account";
        }
        return "redirect:tool_1/result";
    }

    @GetMapping("result")
    public String getInput(@RequestParam(defaultValue = "null") String success, Model model){
        model.addAttribute("success", success);
        return "tool_1";
    }

    @GetMapping("run")
    public String getOptions(@RequestParam String url, @RequestParam(defaultValue = "false") Boolean isScript,  Model model){
        model.addAttribute("client_url", url);
        scripts = isScript;
        clientUrl = url;
        return "template";
    }

    @PostMapping("saveElements")
    public String getElements(@RequestBody String elements) {
        String decode = URLDecoder.decode(elements, StandardCharsets.UTF_8);
        List<String> elements1 = task1Service.getElements(decode);

        return "redirect:result?success=true";
    }
}
