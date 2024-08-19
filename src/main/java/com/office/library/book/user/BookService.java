package com.office.library.book.user;

import com.office.library.book.BookVo;
import com.office.library.book.HopeBookVo;
import com.office.library.book.RentalBookVo;
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

    // 도서 대출
    public int rentalBookConfirm(int b_no, int u_m_no) {
        log.info("[BookService] rentalBookConfirm HAS BEEN CALLED");

        int result = bookDao.insertRentalBook(b_no, u_m_no);

        if (result >= 0) {
            bookDao.updateRentalBookAble(b_no);
        }

        return result;
    }

    // 나의 책장
    public List<RentalBookVo> enterBookshelf(int u_m_no) {
        log.info("[BookService] enterBookshelf HAS BEEN CALLED");

        return bookDao.selectRentalBooks(u_m_no);
    }

    // 도서 대출 이력
    public List<RentalBookVo> listupRentalBookHistory(int u_m_no) {
        log.info("[BookService] listupRentalBookHistory HAS BEEN CALLED");

        return bookDao.selectRentalBookHistory(u_m_no);
    }

    // 희망 도서 요청 확인
    public int requestHopeBookConfirm(HopeBookVo hopeBookVo) {
        log.info("[BookService] requestHopeBookConfirm HAS BEEN CALLED");

        return bookDao.insertHopeBook(hopeBookVo);
    }
}
