package com.office.library.book.user;

import com.office.library.book.BookVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Log4j2
@Component("user.BookDao")
public class BookDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

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
            log.error(e.getMessage());
        }

        return bookVos.size() > 0 ? bookVos : null;
    }
}
