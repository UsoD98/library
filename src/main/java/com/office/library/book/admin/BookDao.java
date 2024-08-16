package com.office.library.book.admin;

import com.office.library.book.BookVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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

}
