package com.office.library.admin.member;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
public class AdminMemberService {

    final static public int ADMIN_ACCOUNT_ALREADY_EXIST = 0;
    final static public int ADMIN_ACCOUNT_CREATE_SUCCESS = 1;
    final static public int ADMIN_ACCOUNT_CREATE_FAIL = -1;

    @Autowired
    AdminMemberDao adminMemberDao;

    @Autowired
    JavaMailSenderImpl javaMailSenderImpl;

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

        if (loginedAdminMemberVo != null) {
            log.info("=====ADMIN LOGIN SUCCESS=====");
        } else {
            log.info("=====ADMIN LOGIN FAILED=====");
        }

        return loginedAdminMemberVo;
    }

    // 관리자 목록 요청
    public List<AdminMemberVo> listupAdmin() {
        log.info("==========[AdminMemberService] HAS BEEN CALLED==========");
        log.info("===================METHOD: listupAdmin()===================");

        return adminMemberDao.selectAdmins();
    }

    // 관리자 승인
    public void setAdminApproval(int a_m_no) {
        log.info("==========[AdminMemberService] HAS BEEN CALLED==========");
        log.info("===================METHOD: setAdminApproval()===================");

        int result = adminMemberDao.updateAdminAccount(a_m_no);
    }

    // 관리자 정보 수정 확인
    public int modifyAccountConfirm(AdminMemberVo adminMemberVo) {
        log.info("==========[AdminMemberService] HAS BEEN CALLED==========");
        log.info("===================METHOD: modifyAccountConfirm()===================");

        return adminMemberDao.updateAdminAccount(adminMemberVo);
    }

    public AdminMemberVo getLoginedAdminMemberVo(int a_m_no) {
        log.info("==========[AdminMemberService] HAS BEEN CALLED==========");
        log.info("===================METHOD: getloginedAdminMemberVo()===================");

        return adminMemberDao.selectAdmin(a_m_no);
    }

    // 관리자 비밀번호 찾기 확인
    public int findPasswordConfirm(AdminMemberVo adminMemberVo) {
        log.info("==========[AdminMemberService] HAS BEEN CALLED==========");
        log.info("===================METHOD: findPasswordConfirm()===================");

        AdminMemberVo selectedAdminMemberVo = adminMemberDao.selectAdmin(adminMemberVo.getA_m_id(),
                adminMemberVo.getA_m_name(), adminMemberVo.getA_m_mail());

        int result = 0;

        if (selectedAdminMemberVo != null) {
            String newPassword = createNewPassword();
            result = adminMemberDao.updatePassword(adminMemberVo.getA_m_id(), newPassword);

            if (result > 0) {
                sendNewPasswordByMail(adminMemberVo.getA_m_mail(), newPassword);
            }
        }
        return result;
    }

    // 관리자 비밀번호 새로 생성
    private String createNewPassword() {
        log.info("==========[AdminMemberService] HAS BEEN CALLED==========");
        log.info("===================METHOD: createNewPassword()===================");

        char[] chars = new char[]{
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z'};

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

        log.info("=====NEW PASSWORD: " + stringBuffer.toString() + "=====");
        return stringBuffer.toString();
    }

    // 생성한 새 비밀번호 메일로 보내기
    private void sendNewPasswordByMail(String toMailAddr, String newPassword) {
        log.info("==========[AdminMemberService] HAS BEEN CALLED==========");
        log.info("===================METHOD: sendNewPasswordByMail()===================");

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
