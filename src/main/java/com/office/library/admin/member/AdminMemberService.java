package com.office.library.admin.member;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class AdminMemberService {

    final static public int ADMIN_ACCOUNT_ALREADY_EXIST = 0;
    final static public int ADMIN_ACCOUNT_CREATE_SUCCESS = 1;
    final static public int ADMIN_ACCOUNT_CREATE_FAIL = -1;

    @Autowired
    AdminMemberDao adminMemberDao;

    public int createAccountConfirm(AdminMemberVo adminMemberVo) {
        log.info("==========[AdminMemberService] HAS BEEN CALLED==========");
        log.info("===================METHOD: createAccountConfirm()===================");

        boolean isMember = adminMemberDao.isAdminMember(adminMemberVo.getA_m_id());

        if (!isMember) {
            int result = adminMemberDao.insertAdminAccount(adminMemberVo);

            if (result > 0) {
                return ADMIN_ACCOUNT_CREATE_SUCCESS;
            } else {
                return ADMIN_ACCOUNT_CREATE_FAIL;
            }
        } else {
            return ADMIN_ACCOUNT_ALREADY_EXIST;
        }
    }

    public AdminMemberVo loginConfirm(AdminMemberVo adminMemberVo) {
        log.info("==========[AdminMemberService] HAS BEEN CALLED==========");
        log.info("===================METHOD: loginConfirm()===================");

        AdminMemberVo loginedAdminMemberVo = adminMemberDao.selectAdmin(adminMemberVo);

        if(loginedAdminMemberVo != null) {
            log.info("=====ADMIN LOGIN SUCCESS=====");
        } else {
            log.info("=====ADMIN LOGIN FAILED=====");
        }

        return loginedAdminMemberVo;
    }
}
