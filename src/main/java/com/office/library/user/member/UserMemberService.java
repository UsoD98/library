package com.office.library.user.member;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.Date;

@Log4j2
@Service
public class UserMemberService {

    final static public int USER_ACCOUNT_ALREADY_EXIST = 0;
    final static public int USER_ACCOUNT_CREATE_SUCCESS = 1;
    final static public int USER_ACCOUNT_CREATE_FAIL = -1;

    @Autowired
    UserMemberDao userMemberDao;

    @Autowired
    JavaMailSenderImpl javaMailSenderImpl;

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

    // 회원정보 수정 확인
    public int modifyAccountConfirm(UserMemberVo userMemberVo) {
        log.info("[UserMemberService] modifyAccountConfirm HAS BEEN CALLED");

        return userMemberDao.updateUserAccount(userMemberVo);
    }

    // 업데이트된 회원정보 가져오기
    public UserMemberVo getLoginedUserMemberVo(int u_m_no) {
        log.info("[UserMemberService] getLoginedUserMemberVo HAS BEEN CALLED");

        return userMemberDao.selectUser(u_m_no);
    }

    // 비밀번호 찾기
    public int findPasswordConfirm(UserMemberVo userMemberVo) {
        log.info("[UserMemberService] findPasswordConfirm HAS BEEN CALLED");

        UserMemberVo selectedUserMemberVo =
                userMemberDao.selectUser(
                        userMemberVo.getU_m_id(),
                        userMemberVo.getU_m_name(),
                        userMemberVo.getU_m_mail()
                );

        int result = 0;

        if (selectedUserMemberVo != null) {

            String newPassword = createNewPassword();
            result = userMemberDao.updatePassword(userMemberVo.getU_m_id(), newPassword);

            if (result > 0) {
                sendNewPasswordByMail(userMemberVo.getU_m_mail(), newPassword);
            }
        }

        return result;
    }

    // 새 비밀번호 생성
    private String createNewPassword() {
        log.info("[UserMemberService] createNewPassword HAS BEEN CALLED");

        char[] chars = new char[]{
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z'
        };

        StringBuffer stringBuffer = new StringBuffer();
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(new Date().getTime());

        int index = 0;
        int length = chars.length;

        for (int i = 0; i < 8; i++) {
            index = secureRandom.nextInt(length);

            if (index % 2 == 0) {
                stringBuffer.append(String.valueOf(chars[index]).toUpperCase());
            } else {
                stringBuffer.append(String.valueOf(chars[index]).toLowerCase());
            }
        }

        log.info("[UserMemberService] createNewPassword: " + stringBuffer.toString());

        return stringBuffer.toString();
    }

    // 새 비밀번호 메일로 전송
    private void sendNewPasswordByMail(String toMailAddr, String newPassword) {
        log.info("[UserMemberService] sendNewPasswordByMail HAS BEEN CALLED");

        final MimeMessagePreparator mimeMessagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                mimeMessageHelper.setTo(toMailAddr);
                mimeMessageHelper.setSubject("[한국도서관] 새 비밀번호 안내입니다.");
                mimeMessageHelper.setText("새 비밀번호: " + newPassword, true);
            }
        };
        javaMailSenderImpl.send(mimeMessagePreparator);
    }
}
