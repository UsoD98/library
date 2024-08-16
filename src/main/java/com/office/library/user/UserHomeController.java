package com.office.library.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/user")
public class UserHomeController {

    @GetMapping({"", "/"})
    public String home() {
        log.info("[UserHomeController] home HAS BEEN CALLED");

        String nextPage = "user/home";

        return nextPage;
    }

}
