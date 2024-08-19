package com.office.library.book.user;

import com.office.library.book.BookVo;
import com.office.library.book.HopeBookVo;
import com.office.library.book.RentalBookVo;
import com.office.library.user.member.UserMemberVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Log4j2
@Controller("user.BookController")
@RequestMapping("/book/user")
public class BookController {

    @Autowired
    BookService bookService;

    // 도서 검색
    @GetMapping("/searchBookConfirm")
    public String searchBookConfirm(BookVo bookVo, Model model) {
        log.info("[BookController] searchBookConfirm HAS BEEN CALLED");

        String nextPage = "user/book/search_book";

        List<BookVo> bookVos = bookService.searchBookConfirm(bookVo);

        model.addAttribute("bookVos", bookVos);

        return nextPage;
    }

    // 도서 상세
    @GetMapping("/bookDetail")
    public String bookDetail(@RequestParam("b_no") int b_no, Model model) {
        log.info("[BookController] bookDetail HAS BEEN CALLED");

        String nextPage = "user/book/book_detail";

        BookVo bookVo = bookService.bookDetail(b_no);

        model.addAttribute("bookVo", bookVo);

        return nextPage;
    }

    // 도서 대출
    @GetMapping("/rentalBookConfirm")
    public String rentalBookConfirm(@RequestParam("b_no") int b_no, HttpSession session) {
        log.info("[BookController] rentalBookConfirm HAS BEEN CALLED");

        String nextPage = "user/book/rental_book_ok";

        UserMemberVo loginedUserMemberVo = (UserMemberVo) session.getAttribute("loginedUserMemberVo");

//        인터셉터로 대체하여 더 이상 컨트롤러에서 로그인 여부를 확인하지 않음
//        if (loginedUserMemberVo == null) {
//            return "redirect:/user/member/loginForm";
//        }

        int result = bookService.rentalBookConfirm(b_no, loginedUserMemberVo.getU_m_no());

        if (result <= 0) {
            nextPage = "user/book/rental_book_ng";
        }

        return nextPage;
    }

    // 나의 책장(인터셉트 필요)
    @GetMapping("/enterBookshelf")
    public String enterBookshelf(HttpSession session, Model model) {
        log.info("[BookController] enterBookshelf HAS BEEN CALLED");

        String nextPage = "user/book/bookshelf";

        UserMemberVo loginedUserMemberVo = (UserMemberVo) session.getAttribute("loginedUserMemberVo");

        List<RentalBookVo> rentalBookVos = bookService.enterBookshelf(loginedUserMemberVo.getU_m_no());

        model.addAttribute("rentalBookVos", rentalBookVos);

        return nextPage;
    }

    // 도서 대출 이력(인터셉트 필요)
    @GetMapping("/listupRentalBookHistory")
    public String listupRentalBookHistory(HttpSession session, Model model) {
        log.info("[BookController] listupRentalBookHistory HAS BEEN CALLED");

        String nextPage = "user/book/rental_book_history";

        UserMemberVo loginedUserMemberVo = (UserMemberVo) session.getAttribute("loginedUserMemberVo");

        List<RentalBookVo> rentalBookVos = bookService.listupRentalBookHistory(loginedUserMemberVo.getU_m_no());

        model.addAttribute("rentalBookVos", rentalBookVos);

        return nextPage;
    }

    // 희망 도서 요청(인터셉트 필요)
    @GetMapping("/requestHopeBookForm")
    public String requestHopeBookForm() {
        log.info("[BookController] requestHopeBookForm HAS BEEN CALLED");

        String nextPage = "user/book/request_hope_book_form";

        return nextPage;
    }

    // 희망 도서 요청 확인(인터셉트 필요)
    @GetMapping("/requestHopeBookConfirm")
    public String requestHopeBookConfirm(HopeBookVo hopeBookVo, HttpSession session) {
        log.info("[BookController] requestHopeBookConfirm HAS BEEN CALLED");

        String nextPage = "user/book/request_hope_book_ok";

        UserMemberVo loginedUserMemberVo = (UserMemberVo) session.getAttribute("loginedUserMemberVo");
        hopeBookVo.setU_m_no(loginedUserMemberVo.getU_m_no());

        int result = bookService.requestHopeBookConfirm(hopeBookVo);

        if(result <= 0) {
            nextPage = "user.book/request_hope_book_ng";
        }

        return nextPage;
    }
}
