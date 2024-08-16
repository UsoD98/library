package com.office.library.admin.member;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
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

        String sql;
        if (adminMemberVo.getA_m_id().equals("super admin")) {
            sql = "INSERT INTO tbl_admin_member(a_m_approval, a_m_id, a_m_pw, a_m_name, a_m_gender, a_m_part, a_m_position, a_m_mail, a_m_phone, a_m_reg_date, a_m_mod_date) "
                    + "VALUES (1, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        } else {
            sql = "INSERT INTO tbl_admin_member(a_m_id, a_m_pw, a_m_name, a_m_gender, a_m_part, a_m_position, a_m_mail, a_m_phone, a_m_reg_date, a_m_mod_date) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        }

        args.add(adminMemberVo.getA_m_id());
        args.add(passwordEncoder.encode(adminMemberVo.getA_m_pw()));
        args.add(adminMemberVo.getA_m_name());
        args.add(adminMemberVo.getA_m_gender());
        args.add(adminMemberVo.getA_m_part());
        args.add(adminMemberVo.getA_m_position());
        args.add(adminMemberVo.getA_m_mail());
        args.add(adminMemberVo.getA_m_phone());

        int result = -1;

        try {
            result = jdbcTemplate.update(sql, args.toArray());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }

    public AdminMemberVo selectAdmin(AdminMemberVo adminMemberVo) {
        log.info("==========[AdminMemberDao] HAS BEEN CALLED==========");
        log.info("===================METHOD: selectAdmin()===================");

        String sql = "SELECT * FROM tbl_admin_member "
                + "WHERE a_m_id = ? AND a_m_approval > 0";

        List<AdminMemberVo> adminMemberVos = new ArrayList<>();

        try {
            adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {
                @Override
                public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {

                    AdminMemberVo adminMemberVo = new AdminMemberVo();

                    adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
                    adminMemberVo.setA_m_approval(rs.getInt("a_m_approval"));
                    adminMemberVo.setA_m_id(rs.getString("a_m_id"));
                    adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
                    adminMemberVo.setA_m_name(rs.getString("a_m_name"));
                    adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
                    adminMemberVo.setA_m_part(rs.getString("a_m_part"));
                    adminMemberVo.setA_m_position(rs.getString("a_m_position"));
                    adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
                    adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
                    adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
                    adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));

                    return adminMemberVo;
                }
            }, adminMemberVo.getA_m_id());

            if (!passwordEncoder.matches(adminMemberVo.getA_m_pw(),
                    adminMemberVos.get(0).getA_m_pw()))
                adminMemberVos.clear();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return adminMemberVos.size() > 0 ? adminMemberVos.get(0) : null;
    }

    // 관리자 목록 요청
    public List<AdminMemberVo> selectAdmins() {
        log.info("==========[AdminMemberDao] HAS BEEN CALLED==========");
        log.info("===================METHOD: selectAdmins()===================");

        String sql = "SELECT * FROM tbl_admin_member";

        List<AdminMemberVo> adminMemberVos = new ArrayList<>();

        try {
            adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {
                @Override
                public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
                    AdminMemberVo adminMemberVo = new AdminMemberVo();

                    adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
                    adminMemberVo.setA_m_approval(rs.getInt("a_m_approval"));
                    adminMemberVo.setA_m_id(rs.getString("a_m_id"));
                    adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
                    adminMemberVo.setA_m_name(rs.getString("a_m_name"));
                    adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
                    adminMemberVo.setA_m_part(rs.getString("a_m_part"));
                    adminMemberVo.setA_m_position(rs.getString("a_m_position"));
                    adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
                    adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
                    adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
                    adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));

                    return adminMemberVo;
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return adminMemberVos;
    }

    // 관리자 승인(a_m_approval 값 0에서 1로 변경)
    public int updateAdminAccount(int a_m_no) {
        log.info("==========[AdminMemberDao] HAS BEEN CALLED==========");
        log.info("===================METHOD: updateAdminAccount()===================");

        String sql = "UPDATE tbl_admin_member SET "
                + "a_m_approval = 1 "
                + "WHERE a_m_no = ?";

        int result = -1;

        try {
            result = jdbcTemplate.update(sql, a_m_no);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }

    // 관리자 정보 수정
    public int updateAdminAccount(AdminMemberVo adminMemberVo) {
        log.info("==========[AdminMemberDao] HAS BEEN CALLED==========");
        log.info("===================METHOD: updateAdminAccount()===================");

        String sql = "UPDATE tbl_admin_member SET "
                + "a_m_name = ?, "
                + "a_m_gender = ?, "
                + "a_m_part = ?, "
                + "a_m_position = ?, "
                + "a_m_mail = ?, "
                + "a_m_phone = ?, "
                + "a_m_mod_date = NOW() "
                + "WHERE a_m_no = ?";

        int result = -1;

        try {
            result = jdbcTemplate.update(sql,
                    adminMemberVo.getA_m_name(),
                    adminMemberVo.getA_m_gender(),
                    adminMemberVo.getA_m_part(),
                    adminMemberVo.getA_m_position(),
                    adminMemberVo.getA_m_mail(),
                    adminMemberVo.getA_m_phone(),
                    adminMemberVo.getA_m_no());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }

    public AdminMemberVo selectAdmin(int a_m_no) {
        log.info("==========[AdminMemberDao] HAS BEEN CALLED==========");
        log.info("===================METHOD: selectAdmin()===================");

        String sql = "SELECT * FROM tbl_admin_member "
                + "WHERE a_m_no = ?";

        List<AdminMemberVo> adminMemberVos = new ArrayList<>();

        try {
            adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {
                @Override
                public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {

                    AdminMemberVo adminMemberVo = new AdminMemberVo();

                    adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
                    adminMemberVo.setA_m_approval(rs.getInt("a_m_approval"));
                    adminMemberVo.setA_m_id(rs.getString("a_m_id"));
                    adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
                    adminMemberVo.setA_m_name(rs.getString("a_m_name"));
                    adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
                    adminMemberVo.setA_m_part(rs.getString("a_m_part"));
                    adminMemberVo.setA_m_position(rs.getString("a_m_position"));
                    adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
                    adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
                    adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
                    adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));

                    return adminMemberVo;
                }
            }, a_m_no);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return adminMemberVos.size() > 0 ? adminMemberVos.get(0) : null;
    }

    // 관리자를 인증하는 selectAdmin 메서드를 오버로딩
    public AdminMemberVo selectAdmin(String a_m_id, String a_m_name, String a_m_mail) {
        log.info("==========[AdminMemberDao] HAS BEEN CALLED==========");
        log.info("===================METHOD: selectAdmin()===================");

        String sql = "SELECT * FROM tbl_admin_member "
                + "WHERE a_m_id = ? AND a_m_name = ? AND a_m_mail = ?";

        List<AdminMemberVo> adminMemberVos = new ArrayList<>();

        try {
            adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {
                @Override
                public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
                    AdminMemberVo adminMemberVo = new AdminMemberVo();

                    adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
                    adminMemberVo.setA_m_approval(rs.getInt("a_m_approval"));
                    adminMemberVo.setA_m_id(rs.getString("a_m_id"));
                    adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
                    adminMemberVo.setA_m_name(rs.getString("a_m_name"));
                    adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
                    adminMemberVo.setA_m_part(rs.getString("a_m_part"));
                    adminMemberVo.setA_m_position(rs.getString("a_m_position"));
                    adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
                    adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
                    adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
                    adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));

                    return adminMemberVo;
                }
            }, a_m_id, a_m_name, a_m_mail);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return adminMemberVos.size() > 0 ? adminMemberVos.get(0) : null;
    }

    // 관리자 새 비밀번호 업데이트
    public int updatePassword(String a_m_id, String newPassword) {
        log.info("==========[AdminMemberDao] HAS BEEN CALLED==========");
        log.info("===================METHOD: updatePassword()===================");

        String sql = "UPDATE tbl_admin_member SET "
                + "a_m_pw = ?, a_m_mod_date = NOW() "
                + "WHERE a_m_id = ?";

        int result = -1;

        try {
            result = jdbcTemplate.update(sql, passwordEncoder.encode(newPassword), a_m_id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }
}
