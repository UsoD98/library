package com.office.library.user.member;

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

    // 로그인 확인
    public UserMemberVo selectUser(UserMemberVo userMemberVo) {
        log.info("[UserMemberDao] selectUser HAS BEEN CALLED");

        String sql = "SELECT * FROM tbl_user_member "
                + "WHERE u_m_id = ?";

        List<UserMemberVo> userMemberVos = new ArrayList<>();

        try {
            userMemberVos = jdbcTemplate.query(sql, new RowMapper<UserMemberVo>() {
                @Override
                public UserMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {

                    UserMemberVo userMemberVo = new UserMemberVo();

                    userMemberVo.setU_m_no(rs.getInt("u_m_no"));
                    userMemberVo.setU_m_id(rs.getString("u_m_id"));
                    userMemberVo.setU_m_pw(rs.getString("u_m_pw"));
                    userMemberVo.setU_m_name(rs.getString("u_m_name"));
                    userMemberVo.setU_m_gender(rs.getString("u_m_gender"));
                    userMemberVo.setU_m_mail(rs.getString("u_m_mail"));
                    userMemberVo.setU_m_phone(rs.getString("u_m_phone"));
                    userMemberVo.setU_m_reg_date(rs.getString("u_m_reg_date"));
                    userMemberVo.setU_m_mod_date(rs.getString("u_m_mod_date"));

                    return userMemberVo;
                }
            }, userMemberVo.getU_m_id());

            if (!passwordEncoder.matches(userMemberVo.getU_m_pw(), userMemberVos.get(0).getU_m_pw())) {
                userMemberVos.clear();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return userMemberVos.size() > 0 ? userMemberVos.get(0) : null;
    }

    // 회원정보 수정
    public int updateUserAccount(UserMemberVo userMemberVo) {
        log.info("[UserMemberDao] updateUserAccount HAS BEEN CALLED");

        String sql = "UPDATE tbl_user_member SET "
                + "u_m_name = ?, "
                + "u_m_gender = ?, "
                + "u_m_mail = ?, "
                + "u_m_phone = ?, "
                + "u_m_mod_date = NOW() "
                + "WHERE u_m_no = ?";

        int result = -1;

        try {
            result = jdbcTemplate.update(sql,
                    userMemberVo.getU_m_name(),
                    userMemberVo.getU_m_gender(),
                    userMemberVo.getU_m_mail(),
                    userMemberVo.getU_m_phone(),
                    userMemberVo.getU_m_no());

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }

    // 회원정보 가져오기
    public UserMemberVo selectUser(int u_m_no) {
        log.info("[UserMemberDao] selectUser HAS BEEN CALLED");

        String sql = "SELECT * FROM tbl_user_member "
                + "WHERE u_m_no = ?";

        List<UserMemberVo> userMemberVos = new ArrayList<>();

        try {
            userMemberVos = jdbcTemplate.query(sql, new RowMapper<UserMemberVo>() {
                @Override
                public UserMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {

                    UserMemberVo userMemberVo = new UserMemberVo();

                    userMemberVo.setU_m_no(rs.getInt("u_m_no"));
                    userMemberVo.setU_m_id(rs.getString("u_m_id"));
                    userMemberVo.setU_m_pw(rs.getString("u_m_pw"));
                    userMemberVo.setU_m_name(rs.getString("u_m_name"));
                    userMemberVo.setU_m_gender(rs.getString("u_m_gender"));
                    userMemberVo.setU_m_mail(rs.getString("u_m_mail"));
                    userMemberVo.setU_m_phone(rs.getString("u_m_phone"));
                    userMemberVo.setU_m_reg_date(rs.getString("u_m_reg_date"));
                    userMemberVo.setU_m_mod_date(rs.getString("u_m_mod_date"));

                    return userMemberVo;
                }
            }, u_m_no);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return userMemberVos.size() > 0 ? userMemberVos.get(0) : null;
    }

    // 사용자가 입력한 정보(아이디, 이름, 메일주소)와 일치하는 회원 조회
    public UserMemberVo selectUser(String u_m_id, String u_m_name, String u_m_mail) {
        log.info("[UserMemberDao] selectUser HAS BEEN CALLED");

        String sql = "SELECT *FROM tbl_user_member "
                + "WHERE u_m_id = ? AND u_m_name = ? AND u_m_mail = ?";

        List<UserMemberVo> userMemberVos = new ArrayList<>();

        try {
            userMemberVos = jdbcTemplate.query(sql, new RowMapper<UserMemberVo>() {
                @Override
                public UserMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {

                    UserMemberVo userMemberVo = new UserMemberVo();

                    userMemberVo.setU_m_no(rs.getInt("u_m_no"));
                    userMemberVo.setU_m_id(rs.getString("u_m_id"));
                    userMemberVo.setU_m_pw(rs.getString("u_m_pw"));
                    userMemberVo.setU_m_name(rs.getString("u_m_name"));
                    userMemberVo.setU_m_gender(rs.getString("u_m_gender"));
                    userMemberVo.setU_m_mail(rs.getString("u_m_mail"));
                    userMemberVo.setU_m_phone(rs.getString("u_m_phone"));
                    userMemberVo.setU_m_reg_date(rs.getString("u_m_reg_date"));
                    userMemberVo.setU_m_mod_date(rs.getString("u_m_mod_date"));

                    return userMemberVo;
                }
            }, u_m_id, u_m_name, u_m_mail);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return userMemberVos.size() > 0 ? userMemberVos.get(0) : null;
    }

    // 발급받은 비밀번호로 회원의 비밀번호 변경
    public int updatePassword(String u_m_id, String newPassword) {
        log.info("[UserMemberDao] updatePassword HAS BEEN CALLED");

        String sql = "UPDATE tbl_user_member SET "
                + "u_m_pw = ?, u_m_mod_date = NOW() "
                + "WHERE u_m_id = ?";

        int result = -1;

        try {
            result = jdbcTemplate.update(sql, passwordEncoder.encode(newPassword), u_m_id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }
}
