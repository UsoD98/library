package com.office.library.admin.member;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Log4j2
@Controller
@RequestMapping("/admin/member")
public class AdminMemberController {

    @Autowired
    AdminMemberService adminMemberService;

    // 회원가입
    @RequestMapping(value = "/createAccountForm", method = RequestMethod.GET)
    public String createAccountForm() {
        log.info("==========[AdminMemberController] HAS BEEN CALLED==========");
        log.info("===================METHOD: createAccountForm()===================");

        String nextPage = "admin/member/create_account_form";

        return nextPage;
    }

    // 회원가입 확인
    @RequestMapping(value = "/createAccountConfirm", method = RequestMethod.POST)
    public String createAccountConfirm(AdminMemberVo adminMemberVo) {
        log.info("==========[AdminMemberController] HAS BEEN CALLED==========");
        log.info("===================METHOD: createAccountConfirm()===================");

        String nextPage = "admin/member/create_account_ok";

        int result = adminMemberService.createAccountConfirm(adminMemberVo);

        if (result <= 0) {
            nextPage = "admin/member/create_account_ng";
        }

        log.info("=====ADMIN ACCOUNT CREATED SUCCESSFULLY=====");
        return nextPage;
    }

}
