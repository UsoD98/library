package com.office.library.admin.member;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class AdminMemberDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean isAdminMember(String a_m_id) {
        log.info("==========[AdminMemberDao] HAS BEEN CALLED==========");
        log.info("===================METHOD: isAdminMember()===================");

        String sql = "SELECT COUNT(*) FROM tbl_admin_member "
                + "WHERE a_m_id = ?";

        int result = jdbcTemplate.queryForObject(sql, Integer.class, a_m_id);

        return result > 0;
    }

    public int insertAdminAccount(AdminMemberVo adminMemberVo) {
        log.info("==========[AdminMemberDao] HAS BEEN CALLED==========");
        log.info("===================METHOD: insertAdminAccount()===================");

        List<String> args = new ArrayList<>();

        String sql = "INSERT INTO tbl_admin_member(";
        if (adminMemberVo.getA_m_id().equals("super admin")) {
            sql += "a_m_approval, ";
            args.add("1");
        }

        sql += "a_m_id, ";
        args.add(adminMemberVo.getA_m_id());

        sql += "a_m_pw, ";
//        args.add(adminMemberVo.getA_m_pw());
        args.add(passwordEncoder.encode(adminMemberVo.getA_m_pw()));

        sql += "a_m_name, ";
        args.add(adminMemberVo.getA_m_name());

        sql += "a_m_gender, ";
        args.add(adminMemberVo.getA_m_gender());

        sql += "a_m_part, ";
        args.add(adminMemberVo.getA_m_part());

        sql += "a_m_position, ";
        args.add(adminMemberVo.getA_m_position());

        sql += "a_m_mail, ";
        args.add(adminMemberVo.getA_m_mail());

        sql += "a_m_phone, ";
        args.add(adminMemberVo.getA_m_phone());

        sql += "a_m_reg_date, a_m_mod_date) ";

        if (adminMemberVo.getA_m_id().equals("super admin")) {
            sql += "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        } else {
            sql += "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        }

        int result = -1;

        try {
            result = jdbcTemplate.update(sql, args.toArray());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }
}
