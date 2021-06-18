package com.peklo.peklo.models.task_1;

import com.peklo.peklo.exceptions.UrlNotConnection;
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
import java.util.stream.Collectors;

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
    public Html getHtml() throws UrlNotConnection {
        Document document = task3Service.getJSoupConnection(clientUrl);
        this.document = document;
        return task1Service.drawSite(document, scripts);
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
    public String getElements(Principal principal, @RequestBody String elements, @RequestParam String time) {
        User user = userService.findByEmail(principal.getName());
        String decode = URLDecoder.decode(elements, StandardCharsets.UTF_8);
        List<String> elements1 = task1Service.getElements(decode);
//        task1Service.saveElements(document, elements1, time, user.getChatId());
        return "redirect:result?success=true";
    }

    @GetMapping("client-items")
    public String getClientItems(Principal principal, Model model){
//        User user = userService.findByEmail(principal.getName());
//        List<Tool1ItemDto> items = task1Service.getItemsWithUserChatId(user.getChatId()).stream()
//                .map(Tool1ItemDto::from)
//                .collect(Collectors.toList());
//        model.addAttribute("clientItems", items);
        return "tool_1_items";
    }

    @PostMapping("delete-item")
    public String deleteElement(@RequestParam Long id){
//        task1Service.deleteItem(id);
        return "redirect:/tool_1/client-items";
    }
}
