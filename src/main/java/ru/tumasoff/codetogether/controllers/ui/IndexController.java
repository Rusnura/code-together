package ru.tumasoff.codetogether.controllers.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
  @GetMapping("/chat") // FIXME: Remove me
  public String chat() {
    return "index";
  }

  @GetMapping("/{roomId}")
  public String index(@PathVariable String roomId,
                      @RequestParam String name,
                      ModelMap map) {
    map.addAttribute("name", name);
    map.addAttribute("roomId", roomId);
    return "code-together";
  }
}
