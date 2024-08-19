package com.office.library.book.admin;

import com.office.library.book.BookVo;
import com.office.library.book.HopeBookVo;
import com.office.library.book.RentalBookVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
            RowMapper<BookVo> rowMapper = BeanPropertyRowMapper.newInstance(BookVo.class);
            bookVos = jdbcTemplate.query(sql, rowMapper, "%" + bookVo.getB_name() + "%");
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
            RowMapper<BookVo> rowMapper = BeanPropertyRowMapper.newInstance(BookVo.class);
            bookVos = jdbcTemplate.query(sql, rowMapper, b_no);
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
            RowMapper<RentalBookVo> rowMapper = BeanPropertyRowMapper.newInstance(RentalBookVo.class);
            rentalBookVos = jdbcTemplate.query(sql, rowMapper);
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
            RowMapper<HopeBookVo> rowMapper = BeanPropertyRowMapper.newInstance(HopeBookVo.class);
            hopeBookVos = jdbcTemplate.query(sql, rowMapper);
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

    // 전체도서 목록 조회
    public List<BookVo> selectAllBooks() {
        log.info("[BookDao] selectAllBooks HAS BEEN CALLED");

        String sql = "SELECT * FROM tbl_book "
                + "ORDER BY b_reg_date DESC";

        List<BookVo> bookVos = new ArrayList<>();

        try {
            RowMapper<BookVo> rowMapper = BeanPropertyRowMapper.newInstance(BookVo.class);
            bookVos = jdbcTemplate.query(sql, rowMapper);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return bookVos.size() > 0 ? bookVos : null;
    }
}
