package com.office.library.admin;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Log4j2
@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String home() {
        log.info("==========[AdminHomeController] HAS BEEN CALLED==========");
        log.info("===================METHOD: home()===================");

        String nextPage = "admin/home";

        return nextPage;
    }

}
