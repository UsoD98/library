package com.office.library.user.member;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class UserMemberDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    // 기존 회원인지 확인
    public boolean isUserMember(String u_m_id) {
        log.info("[UserMemberDao] isUserMember HAS BEEN CALLED");

        String sql = "SELECT COUNT(*) FROM tbl_user_member WHERE u_m_id = ?";

        int result = jdbcTemplate.queryForObject(sql, Integer.class, u_m_id);

        return result > 0 ? true : false;
    }

    // 회원가입
    public int insertUserAccount(UserMemberVo userMemberVo) {
        log.info("[UserMemberDao] insertUserAccount HAS BEEN CALLED");

        String sql = "INSERT INTO tbl_user_member(u_m_id, "
                + "u_m_pw, " + "u_m_name, " + "u_m_gender, "
                + "u_m_mail, " + "u_m_phone, " + "u_m_reg_date, "
                + "u_m_mod_date) VALUES(?, ?, ?, ?, ?, ?, NOW(), NOW())";

        int result = -1;

        try {
            result = jdbcTemplate.update(sql,
                    userMemberVo.getU_m_id(),
                    passwordEncoder.encode(userMemberVo.getU_m_pw()),
                    userMemberVo.getU_m_name(),
                    userMemberVo.getU_m_gender(),
                    userMemberVo.getU_m_mail(),
                    userMemberVo.getU_m_phone());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
