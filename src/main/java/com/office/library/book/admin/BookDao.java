package com.office.library.book.admin;

import com.office.library.book.BookVo;
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
@Component
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

        List<String> args = new ArrayList<>();

        String sql = "UPDATE tbl_book SET ";
        if (bookVo.getB_thumbnail() != null) {
            sql += "b_thumbnail = ?, ";
            args.add(bookVo.getB_thumbnail());
        }

        sql += "b_name = ?, ";
        args.add(bookVo.getB_name());

        sql += "b_author = ?, ";
        args.add(bookVo.getB_author());

        sql += "b_publisher = ?, ";
        args.add(bookVo.getB_publisher());

        sql += "b_publish_year = ?, ";
        args.add(bookVo.getB_publish_year());

        sql += "b_isbn = ?, ";
        args.add(bookVo.getB_isbn());

        sql += "b_call_number = ?, ";
        args.add(bookVo.getB_call_number());

        sql += "b_rental_able = ?, ";
        args.add(Integer.toString(bookVo.getB_rental_able()));

        sql += "b_mod_date = NOW() ";

        sql += "WHERE b_no = ?";
        args.add(Integer.toString(bookVo.getB_no()));

        int result = -1;

        try {
            result = jdbcTemplate.update(sql, args.toArray());
        } catch (Exception e) {
            log.info(e.getMessage());
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
}
