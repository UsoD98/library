package com.office.library.admin.member;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

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
            log.info("=====ADMIN ACCOUNT CREATION FAILED=====");
        } else {
            log.info("=====ADMIN ACCOUNT CREATED SUCCESSFULLY=====");
        }
        return nextPage;
    }

    // 로그인
    @GetMapping("loginForm")
    public String loginForm() {
        log.info("==========[AdminMemberController] HAS BEEN CALLED==========");
        log.info("===================METHOD: loginForm()===================");

        String nextPage = "admin/member/login_form";

        return nextPage;
    }

    // 로그인 확인
    @PostMapping("loginConfirm")
    public String loginConfirm(AdminMemberVo adminMemberVo, HttpSession session) {
        log.info("==========[AdminMemberController] HAS BEEN CALLED==========");
        log.info("===================METHOD: loginConfirm()===================");

        String nextPage = "admin/member/login_ok";

        AdminMemberVo loginedAdminMemberVo = adminMemberService.loginConfirm(adminMemberVo);

        if (loginedAdminMemberVo == null) {
            nextPage = "admin/member/login_ng";
            log.info("=====ADMIN LOGIN FAILED=====");
        } else {
            log.info("=====ADMIN LOGIN SUCCESSFUL=====");
            session.setAttribute("loginedAdminMemberVo", loginedAdminMemberVo);
            session.setMaxInactiveInterval(60 * 30);
        }

        return nextPage;
    }

    // 로그아웃 확인
    @GetMapping("/logoutConfirm")
    public String logoutConfirm(HttpSession session) {
        log.info("==========[AdminMemberController] HAS BEEN CALLED==========");
        log.info("===================METHOD: logoutConfirm()===================");

        String nextPage = "redirect:/admin";

        session.invalidate();
        log.info("=====ADMIN LOGOUT SUCCESSFUL=====");

        return nextPage;
    }

    // 관리자 목록 요청
    @GetMapping("/listupAdmin")
    public String listupAdmin(Model model) {
        log.info("==========[AdminMemberController] HAS BEEN CALLED==========");
        log.info("===================METHOD: listupAdmin()===================");

        String nextPage = "admin/member/listup_admins";

        List<AdminMemberVo> adminMemberVos = adminMemberService.listupAdmin();

        model.addAttribute("adminMemberVos", adminMemberVos);

        return nextPage;
    }

    // 관리자 승인
    @GetMapping("/setAdminApproval")
    public String setAdminApproval(@RequestParam("a_m_no") int a_m_no) {
        log.info("==========[AdminMemberController] HAS BEEN CALLED==========");
        log.info("===================METHOD: setAdminApproval()===================");

        String nextPage = "redirect:/admin/member/listupAdmin";

        adminMemberService.setAdminApproval(a_m_no);
        log.info("관리자 승인이 완료되었습니다 : " + a_m_no);

        return nextPage;
    }

    // 관리자 회원 정보 수정
    @GetMapping("/modifyAccountForm")
    public String modifyAccountForm(HttpSession session) {
        log.info("==========[AdminMemberController] HAS BEEN CALLED==========");
        log.info("===================METHOD: modifyAccountForm()===================");

        String nextPage = "admin/member/modify_account_form";

//        인터셉터로 대체
//        AdminMemberVo loginedAdminMemberVo = (AdminMemberVo) session.getAttribute("loginedAdminMemberVo");
//
//        if (loginedAdminMemberVo == null) {
//            nextPage = "redirect:/admin/member/loginForm";
//        }

        return nextPage;
    }

    // 관리자 회원 정보 수정 확인
    @PostMapping("/modifyAccountConfirm")
    public String modifyAccountConfirm(AdminMemberVo adminMemberVo, HttpSession session) {
        log.info("==========[AdminMemberController] HAS BEEN CALLED==========");
        log.info("===================METHOD: modifyAccountConfirm()===================");

        String nextPage = "admin/member/modify_account_ok";

        int result = adminMemberService.modifyAccountConfirm(adminMemberVo);

        if (result > 0) {
            AdminMemberVo loginedAdminMemberVo = adminMemberService.getLoginedAdminMemberVo(adminMemberVo.getA_m_no());

            session.setAttribute("loginedAdminMemberVo", loginedAdminMemberVo);
            session.setMaxInactiveInterval(60 * 30);
            log.info("=====ADMIN ACCOUNT MODIFICATION SUCCESSFUL=====");
        } else {
            nextPage = "admin/member/modify_account_ng";
            log.info("=====ADMIN ACCOUNT MODIFICATION FAILED=====");
        }

        return nextPage;
    }

    // 관리자 비밀번호 찾기
    @GetMapping("/findPasswordForm")
    public String findPasswordForm() {
        log.info("==========[AdminMemberController] HAS BEEN CALLED==========");
        log.info("===================METHOD: findPasswordForm()===================");

        String nextPage = "admin/member/find_password_form";

        return nextPage;
    }

    // 관리자 비밀번호 찾기 확인
    @PostMapping("/findPasswordConfirm")
    public String findPasswordConfirm(AdminMemberVo adminMemberVo) {
        log.info("==========[AdminMemberController] HAS BEEN CALLED==========");
        log.info("===================METHOD: findPasswordConfirm()===================");

        String nextPage = "admin/member/find_password_ok";

        int result = adminMemberService.findPasswordConfirm(adminMemberVo);

        if (result <= 0) {
            nextPage = "admin/member/find_password_ng";
        }

        return nextPage;
    }
}
