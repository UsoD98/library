package com.office.library.book.admin;

import com.office.library.book.BookVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class BookService {

    final static public int BOOK_ISBN_ALREADY_EXIST = 0;
    final static public int BOOK_REGISTER_SUCCESS = 1;
    final static public int BOOK_REGISTER_FAIL = -1;

    @Autowired
    BookDao bookDao;

    public int registerBookConfirm(BookVo bookVo) {
        log.info("[BookService] registerBookConfirm HAS BEEN CALLED");

        boolean isISBN = bookDao.isISBN(bookVo.getB_isbn());

        if (!isISBN) {
            int result = bookDao.insertBook(bookVo);

            if (result > 0) {
                return BOOK_REGISTER_SUCCESS;
            } else {
                return BOOK_REGISTER_FAIL;
            }
        } else {
            return BOOK_ISBN_ALREADY_EXIST;
        }
    }
}
