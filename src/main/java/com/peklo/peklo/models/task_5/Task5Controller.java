package com.peklo.peklo.models.task_5;

import com.peklo.peklo.exceptions.UrlNotCorrect;
import com.peklo.peklo.models.User.User;
import com.peklo.peklo.models.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "tool_5")
public class Task5Controller {
    private final Task5Service service;
    private final UserService userService;

    @GetMapping("result")
    public String tool4(@RequestParam(name = "success", defaultValue = "") String clientError,
                        Model model){
        model.addAttribute("success",clientError);
        return "tool_5";
    }

    @PostMapping("/run")
    public String run (@RequestParam String urlFromFront, @RequestParam String social, Principal principal, @RequestParam String kind){
        if(!urlFromFront.startsWith("https://vk.com") && !urlFromFront.startsWith("https://m.vk.com")){
            throw new UrlNotCorrect();
        }
        User user = userService.findByEmail(principal.getName());
        String id = "";
        if("group".equals(kind)) {
            id = service.cutUrlGroup(urlFromFront);
        } else if("user".equals(kind)) {
            id = service.cutUrlUser(urlFromFront);
        }
        if (id.equals("")) throw new UrlNotCorrect();
        service.start(id, user.getEmail(), kind, urlFromFront);
        return "redirect:result?success=true";
    }
}
