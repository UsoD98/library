package com.office.library.book.admin;

import com.office.library.book.BookVo;
import com.office.library.book.HopeBookVo;
import com.office.library.book.RentalBookVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component("admin.BookDao")
public class BookDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public boolean isISBN(String b_isbn) {
        log.info("[BookDao] isISBN HAS BEEN CALLED");

        String sql = "SELECT COUNT(*) FROM tbl_book "
                + "WHERE b_isbn = ?";

        int result = jdbcTemplate.queryForObject(sql, Integer.class, b_isbn);

        return result > 0 ? true : false;
    }

    public int insertBook(BookVo bookVo) {
        log.info("[BookDao] insertBook HAS BEEN CALLED");

        String sql = "INSERT INTO tbl_book(b_thumbnail, b_name, b_author, b_publisher, b_publish_year, b_isbn, b_call_number, b_rental_able, b_reg_date, b_mod_date) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

        int result = -1;

        try {
            result = jdbcTemplate.update(sql,
                    bookVo.getB_thumbnail(),
                    bookVo.getB_name(),
                    bookVo.getB_author(),
                    bookVo.getB_publisher(),
                    bookVo.getB_publish_year(),
                    bookVo.getB_isbn(),
                    bookVo.getB_call_number(),
                    bookVo.getB_rental_able());
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return result;
    }

    // 도서 검색
    public List<BookVo> selectBooksBySearch(BookVo bookVo) {
        log.info("[BookDao] selectBooksBySearch HAS BEEN CALLED");

        String sql = "SELECT * FROM tbl_book "
                + "WHERE b_name LIKE ? "
                + "ORDER BY b_no DESC";

        List<BookVo> bookVos = null;

        try {
            bookVos = jdbcTemplate.query(sql, new RowMapper<BookVo>() {
                @Override
                public BookVo mapRow(ResultSet rs, int rowNum) throws SQLException {

                    BookVo bookVo = new BookVo();

                    bookVo.setB_no(rs.getInt("b_no"));
                    bookVo.setB_thumbnail(rs.getString("b_thumbnail"));
                    bookVo.setB_name(rs.getString("b_name"));
                    bookVo.setB_author(rs.getString("b_author"));
                    bookVo.setB_publisher(rs.getString("b_publisher"));
                    bookVo.setB_publish_year(rs.getString("b_publish_year"));
                    bookVo.setB_isbn(rs.getString("b_isbn"));
                    bookVo.setB_call_number(rs.getString("b_call_number"));
                    bookVo.setB_rental_able(rs.getInt("b_rental_able"));
                    bookVo.setB_reg_date(rs.getString("b_reg_date"));
                    bookVo.setB_mod_date(rs.getString("b_mod_date"));

                    return bookVo;
                }
            }, "%" + bookVo.getB_name() + "%");
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return bookVos.size() > 0 ? bookVos : null;
    }

    // 도서 상세 조회
    public BookVo selectBook(int b_no) {
        log.info("[BookDao] selectBook HAS BEEN CALLED");

        String sql = "SELECT * FROM tbl_book WHERE b_no = ?";

        List<BookVo> bookVos = null;

        try {
            bookVos = jdbcTemplate.query(sql, new RowMapper<BookVo>() {
                @Override
                public BookVo mapRow(ResultSet rs, int rowNum) throws SQLException {

                    BookVo bookVo = new BookVo();

                    bookVo.setB_no(rs.getInt("b_no"));
                    bookVo.setB_thumbnail(rs.getString("b_thumbnail"));
                    bookVo.setB_name(rs.getString("b_name"));
                    bookVo.setB_author(rs.getString("b_author"));
                    bookVo.setB_publisher(rs.getString("b_publisher"));
                    bookVo.setB_publish_year(rs.getString("b_publish_year"));
                    bookVo.setB_isbn(rs.getString("b_isbn"));
                    bookVo.setB_call_number(rs.getString("b_call_number"));
                    bookVo.setB_rental_able(rs.getInt("b_rental_able"));
                    bookVo.setB_reg_date(rs.getString("b_reg_date"));
                    bookVo.setB_mod_date(rs.getString("b_mod_date"));

                    return bookVo;
                }
            }, b_no);
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return bookVos.size() > 0 ? bookVos.get(0) : null;
    }

    // 도서 수정
    public int updateBook(BookVo bookVo) {
        log.info("[BookDao] updateBook HAS BEEN CALLED");

        String sql;
        List<String> args = new ArrayList<>();


        if (bookVo.getB_thumbnail() != null) {
            sql = "UPDATE tbl_book SET "
                    + "b_thumbnail = ?, "
                    + "b_name = ?, "
                    + "b_author = ?, "
                    + "b_publisher = ?, "
                    + "b_publish_year = ?, "
                    + "b_isbn = ?, "
                    + "b_call_number = ?, "
                    + "b_rental_able = ?, "
                    + "b_mod_date = NOW() "
                    + "WHERE b_no = ?";
            args.add(bookVo.getB_thumbnail());
        } else {
            sql = "UPDATE tbl_book SET "
                    + "b_name = ?, "
                    + "b_author = ?, "
                    + "b_publisher = ?, "
                    + "b_publish_year = ?, "
                    + "b_isbn = ?, "
                    + "b_call_number = ?, "
                    + "b_rental_able = ?, "
                    + "b_mod_date = NOW() "
                    + "WHERE b_no = ?";
        }

        args.add(bookVo.getB_name());
        args.add(bookVo.getB_author());
        args.add(bookVo.getB_publisher());
        args.add(bookVo.getB_publish_year());
        args.add(bookVo.getB_isbn());
        args.add(bookVo.getB_call_number());
        args.add(Integer.toString(bookVo.getB_rental_able()));
        args.add(Integer.toString(bookVo.getB_no()));

        int result = -1;

        try {
            result = jdbcTemplate.update(sql, args.toArray());
        } catch (Exception e) {
            log.error("Error updating book: " + e.getMessage());
        }

        return result;
    }

    // 도서 삭제
    public int deleteBook(int b_no) {
        log.info("[BookDao] deleteBook HAS BEEN CALLED");

        String sql = "DELETE FROM tbl_book "
                + "WHERE b_no = ?";

        int result = -1;

        try {
            result = jdbcTemplate.update(sql, b_no);
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return result;
    }

    // 대출 도서 목록 조회
    public List<RentalBookVo> selectRentalBooks() {
        log.info("[BookDao] selectRentalBooks HAS BEEN CALLED");

        String sql = "SELECT * FROM tbl_rental_book rb "
                + "JOIN tbl_book b "
                + "ON rb.b_no = b.b_no "
                + "JOIN tbl_user_member um "
                + "ON rb.u_m_no = um.u_m_no "
                + "WHERE rb.rb_end_date = '1000-01-01' "
                + "ORDER BY um.u_m_id, rb.rb_reg_date DESC";

        List<RentalBookVo> rentalBookVos = new ArrayList<RentalBookVo>();

        try {
            rentalBookVos = jdbcTemplate.query(sql, new RowMapper<RentalBookVo>() {
                @Override
                public RentalBookVo mapRow(ResultSet rs, int rowNum) throws SQLException {

                    RentalBookVo rentalBookVo = new RentalBookVo();

                    rentalBookVo.setRb_no(rs.getInt("rb_no"));
                    rentalBookVo.setB_no(rs.getInt("b_no"));
                    rentalBookVo.setU_m_no(rs.getInt("u_m_no"));
                    rentalBookVo.setRb_start_date(rs.getString("rb_start_date"));
                    rentalBookVo.setRb_end_date(rs.getString("rb_end_date"));
                    rentalBookVo.setRb_reg_date(rs.getString("rb_reg_date"));
                    rentalBookVo.setRb_mod_date(rs.getString("rb_mod_date"));

                    rentalBookVo.setB_thumbnail(rs.getString("b_thumbnail"));
                    rentalBookVo.setB_name(rs.getString("b_name"));
                    rentalBookVo.setB_author(rs.getString("b_author"));
                    rentalBookVo.setB_publisher(rs.getString("b_publisher"));
                    rentalBookVo.setB_isbn(rs.getString("b_isbn"));
                    rentalBookVo.setB_call_number(rs.getString("b_call_number"));
                    rentalBookVo.setB_rental_able(rs.getInt("b_rental_able"));
                    rentalBookVo.setB_reg_date(rs.getString("b_reg_date"));

                    rentalBookVo.setU_m_id(rs.getString("u_m_id"));
                    rentalBookVo.setU_m_pw(rs.getString("u_m_pw"));
                    rentalBookVo.setU_m_name(rs.getString("u_m_name"));
                    rentalBookVo.setU_m_gender(rs.getString("u_m_gender"));
                    rentalBookVo.setU_m_mail(rs.getString("u_m_mail"));
                    rentalBookVo.setU_m_phone(rs.getString("u_m_phone"));
                    rentalBookVo.setU_m_reg_date(rs.getString("u_m_reg_date"));
                    rentalBookVo.setU_m_mod_date(rs.getString("u_m_mod_date"));

                    return rentalBookVo;
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return rentalBookVos;
    }

    // 대출 도서 반납 처리
    public int updateRentalBook(int rb_no) {
        log.info("[BookDao] updateRentalBook HAS BEEN CALLED");

        String sql = "UPDATE tbl_rental_book "
                + "SET rb_end_date = NOW() "
                + "WHERE rb_no = ?";

        int result = -1;

        try {
            result = jdbcTemplate.update(sql, rb_no);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }

    // 도서 상태를 대출 가능으로 변경
    public int updateBook(int b_no) {
        log.info("[BookDao] updateBook HAS BEEN CALLED");

        String sql = "UPDATE tbl_book "
                + "SET b_rental_able = 1 "
                + "WHERE b_no = ?";

        int result = -1;

        try {
            result = jdbcTemplate.update(sql, b_no);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }

    // 희망 도서 조회
    public List<HopeBookVo> selectHopeBooks() {
        log.info("[BookDao] selectHopeBooks HAS BEEN CALLED");

        String sql = "SELECT * FROM tbl_hope_book hb "
                + "JOIN tbl_user_member um "
                + "ON hb.u_m_no = um.u_m_no "
                + "ORDER BY hb.hb_no DESC";

        List<HopeBookVo> hopeBookVos = new ArrayList<>();

        try {
            hopeBookVos = jdbcTemplate.query(sql, new RowMapper<HopeBookVo>() {
                @Override
                public HopeBookVo mapRow(ResultSet rs, int rowNum) throws SQLException {

                    HopeBookVo hopeBookVo = new HopeBookVo();

                    hopeBookVo.setHb_no(rs.getInt("hb_no"));
                    hopeBookVo.setHb_name(rs.getString("hb_name"));
                    hopeBookVo.setHb_author(rs.getString("hb_author"));
                    hopeBookVo.setHb_publisher(rs.getString("hb_publisher"));
                    hopeBookVo.setHb_publish_year(rs.getString("hb_publish_year"));
                    hopeBookVo.setHb_reg_date(rs.getString("hb_reg_date"));
                    hopeBookVo.setHb_mod_date(rs.getString("hb_mod_date"));
                    hopeBookVo.setHb_result(rs.getInt("hb_result"));
                    hopeBookVo.setHb_result_last_date(rs.getString("hb_result_last_date"));

                    hopeBookVo.setU_m_no(rs.getInt("u_m_no"));
                    hopeBookVo.setU_m_id(rs.getString("u_m_id"));
                    hopeBookVo.setU_m_pw(rs.getString("u_m_pw"));
                    hopeBookVo.setU_m_name(rs.getString("u_m_name"));
                    hopeBookVo.setU_m_gender(rs.getString("u_m_gender"));
                    hopeBookVo.setU_m_mail(rs.getString("u_m_mail"));
                    hopeBookVo.setU_m_phone(rs.getString("u_m_phone"));
                    hopeBookVo.setU_m_reg_date(rs.getString("u_m_reg_date"));
                    hopeBookVo.setU_m_mod_date(rs.getString("u_m_mod_date"));

                    return hopeBookVo;
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return hopeBookVos;
    }

    // 희망 도서 업데이트
    public void updateHopeBookResult(int hb_no) {
        log.info("[BookDao] updateHopeBookResult HAS BEEN CALLED");

        String sql = "UPDATE tbl_hope_book "
                + "SET hb_result = 1, hb_result_last_date = NOW() "
                + "WHERE hb_no = ?";

        try {
            jdbcTemplate.update(sql, hb_no);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
