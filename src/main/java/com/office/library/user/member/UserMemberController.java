package com.office.library.user.member;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

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

    // 로그인
    @GetMapping("/loginForm")
    public String loginForm() {
        log.info("[UserMemberController] loginForm HAS BEEN CALLED");

        String nextPage = "user/member/login_form";

        return nextPage;
    }

    // 로그인 확인
    @PostMapping("/loginConfirm")
    public String loginConfirm(UserMemberVo userMemberVo, HttpSession session) {
        log.info("[UserMemberController] loginConfirm HAS BEEN CALLED");

        String nextPage = "user/member/login_ok";

        UserMemberVo loginedUserMemberVo = userMemberService.loginConfirm(userMemberVo);

        if (loginedUserMemberVo == null) {
            log.info("[UserMemberController] loginConfirm: login failed");
            nextPage = "user/member/login_ng";
        } else {
            log.info("[UserMemberController] loginConfirm: login success");
            session.setAttribute("loginedUserMemberVo", loginedUserMemberVo);
            session.setMaxInactiveInterval(60 * 30);
        }

        return nextPage;
    }

    // 로그아웃 확인
    @GetMapping("/logoutConfirm")
    public String logoutConfirm(HttpSession session) {
        log.info("[UserMemberController] logoutConfirm HAS BEEN CALLED");

        String nextPage = "redirect:/";
        session.invalidate();

        return nextPage;
    }
}
