package com.office.library.user.member;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserMemberService {

    final static public int USER_ACCOUNT_ALREADY_EXIST = 0;
    final static public int USER_ACCOUNT_CREATE_SUCCESS = 1;
    final static public int USER_ACCOUNT_CREATE_FAIL = -1;

    @Autowired
    UserMemberDao userMemberDao;

    // 회원가입 확인
    public int createAccountConfirm(UserMemberVo userMemberVo) {
        log.info("[UserMemberService] createAccountConfirm HAS BEEN CALLED");

        boolean isMember = userMemberDao.isUserMember(userMemberVo.getU_m_id());

        if (!isMember) {
            int result = userMemberDao.insertUserAccount(userMemberVo);

            if (result > 0) {
                return USER_ACCOUNT_CREATE_SUCCESS;
            } else {
                return USER_ACCOUNT_CREATE_FAIL;
            }
        } else {
            return USER_ACCOUNT_ALREADY_EXIST;
        }
    }

    // 로그인 확인
    public UserMemberVo loginConfirm(UserMemberVo userMemberVo) {
        log.info("[UserMemberService] loginConfirm HAS BEEN CALLED");

        UserMemberVo loginedUserMemberVo = userMemberDao.selectUser(userMemberVo);

        if (loginedUserMemberVo != null) {
            log.info("[UserMemberService] loginConfirm SUCCESS");
        } else {
            log.info("[UserMemberService] loginConfirm FAIL");
        }

        return loginedUserMemberVo;
    }
}
