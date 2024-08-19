package com.office.library.book.user;

import com.office.library.book.BookVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service("user.BookService")
public class BookService {

    @Autowired
    BookDao bookDao;

    // 도서 검색
    public List<BookVo> searchBookConfirm(BookVo bookVo) {
        log.info("[BookService] searchBookConfirm HAS BEEN CALLED");

        return bookDao.selectBooksBySearch(bookVo);
    }

    // 도서 상세
    public BookVo bookDetail(int b_no) {
        log.info("[BookService] bookDetail HAS BEEN CALLED");

        return bookDao.selectBook(b_no);
    }
}
