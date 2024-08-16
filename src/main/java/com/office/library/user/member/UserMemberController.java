package com.office.library.user.member;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/user/member")
public class UserMemberController {

    @Autowired
    UserMemberService userMemberService;

    // 회원가입
    @GetMapping("/createAccountForm")
    public String createAccountForm() {
        log.info("[UserMemberController] createAccountForm HAS BEEN CALLED");

        String nextPage = "user/member/create_account_form";

        return nextPage;
    }

    // 회원가입 확인
    @PostMapping("/createAccountConfirm")
    public String createAccountConfirm(UserMemberVo userMemberVo) {
        log.info("[UserMemberController] createAccountConfirm HAS BEEN CALLED");

        String nextPage = "user/member/create_account_ok";

        int result = userMemberService.createAccountConfirm(userMemberVo);

        if (result <= 0) {
            nextPage = "user/member/create_account_ng";
        }

        return nextPage;
    }
}
