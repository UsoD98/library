package com.office.library.book.admin;

import com.office.library.book.BookVo;
import com.office.library.book.RentalBookVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service("admin.BookService")
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

    // 도서 검색
    public List<BookVo> searchBookConfirm(BookVo bookVo) {
        log.info("[BookService] searchBookConfirm HAS BEEN CALLED");

        return bookDao.selectBooksBySearch(bookVo);
    }

    // 도서 상세 조회
    public BookVo bookDetail(int b_no) {
        log.info("[BookService] bookDetail HAS BEEN CALLED");

        return bookDao.selectBook(b_no);
    }

    // 도서 검색 결과 데이터를 수정 폼에 전달
    public BookVo modifyBookForm(int b_no) {
        log.info("[BookService] modifyBookForm HAS BEEN CALLED");

        return bookDao.selectBook(b_no);
    }

    // 도서 수정
    public int modifyBookConfirm(BookVo bookVo) {
        log.info("[BookService] modifyBookConfirm HAS BEEN CALLED");

        return bookDao.updateBook(bookVo);
    }

    // 도서 삭제
    public int deleteBookConfirm(int b_no) {
        log.info("[BookService] deleteBookConfirm HAS BEEN CALLED");

        return bookDao.deleteBook(b_no);
    }

    // 대출 도서 목록
    public List<RentalBookVo> getRentalBooks() {
        log.info("[BookService] getRentalBooks HAS BEEN CALLED");

        return bookDao.selectRentalBooks();
    }

    // 대출 도서 반납
    public int returnBookConfirm(int b_no, int rb_no) {
        log.info("[BookService] returnBookConfirm HAS BEEN CALLED");

        // 대출 도서를 반납 처리
        int result = bookDao.updateRentalBook(rb_no);

        // 도서의 상태를 대출 가능으로 변경
        if (result > 0) {
            result = bookDao.updateBook(b_no);
        }

        return result;
    }
}
