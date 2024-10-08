package com.office.library.book.admin;

import com.office.library.book.BookVo;
import com.office.library.book.HopeBookVo;
import com.office.library.book.RentalBookVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
@Controller("admin.BookController")
@RequestMapping("/book/admin")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    UploadFileService uploadFileService;

    // 도서 등록
    @GetMapping("/registerBookForm")
    public String registerBookForm() {
        log.info("[BookController] registerBookForm HAS BEEN CALLED");

        String nextPage = "admin/book/register_book_form";

        return nextPage;
    }

    // 도서 등록 확인
    @PostMapping("/registerBookConfirm")
    public String registerBookConfirm(BookVo bookVo, @RequestParam("file") MultipartFile file) {
        log.info("[BookController] registerBookConfirm HAS BEEN CALLED");

        String nextPage = "admin/book/register_book_ok";

        // 파일 저장
        String savedFileName = uploadFileService.upload(file);

        if (savedFileName != null) {
            bookVo.setB_thumbnail(savedFileName);
            int result = bookService.registerBookConfirm(bookVo);

            if (result <= 0) {
                nextPage = "admin/book/register_book_ng";
            }
        } else {
            nextPage = "admin/book/register_book_ng";
        }

        return nextPage;
    }

    // 도서 검색
    @GetMapping("/searchBookConfirm")
    public String searchBookConfirm(BookVo bookVo, Model model) {
        log.info("[BookController] searchBookConfirm HAS BEEN CALLED");

        String nextPage = "admin/book/search_book";

        List<BookVo> bookVos = bookService.searchBookConfirm(bookVo);

        model.addAttribute("bookVos", bookVos);

        return nextPage;
    }

    // 도서 상세 조회
    @GetMapping("/bookDetail")
    public String bookDetail(@RequestParam("b_no") int b_no, Model model) {
        log.info("[BookController] bookDetail HAS BEEN CALLED");

        String nextPage = "admin/book/book_detail";

        BookVo bookVo = bookService.bookDetail(b_no);

        model.addAttribute("bookVo", bookVo);

        return nextPage;
    }

    // 도서 수정
    @GetMapping("/modifyBookForm")
    public String modifyBookForm(@RequestParam("b_no") int b_no, Model model) {
        log.info("[BookController] modifyBookForm HAS BEEN CALLED");

        String nextPage = "admin/book/modify_book_form";

        BookVo bookVo = bookService.modifyBookForm(b_no);

        model.addAttribute("bookVo", bookVo);

        return nextPage;
    }

    // 도서 수정 확인
    @PostMapping("/modifyBookConfirm")
    public String modifyBookConfirm(BookVo bookVo, @RequestParam("file") MultipartFile file) {
        log.info("[BookController] modifyBookConfirm HAS BEEN CALLED");

        String nextPage = "admin/book/modify_book_ok";

        if (!file.getOriginalFilename().equals("")) {
            // 파일 저장
            String savedFileName = uploadFileService.upload(file);
            if (savedFileName != null) {
                // 기존 파일 삭제
                String originalFileName = bookService.bookDetail(bookVo.getB_no()).getB_thumbnail();
                uploadFileService.delete(originalFileName);

                bookVo.setB_thumbnail(savedFileName);
            }
        }

        int result = bookService.modifyBookConfirm(bookVo);

        if (result <= 0) {
            nextPage = "admin/book/modify_book_ng";
        }

        return nextPage;
    }

    // 도서 삭제 확인
    @GetMapping("/deleteBookConfirm")
    public String deleteBookConfirm(@RequestParam("b_no") int b_no) {
        log.info("[BookController] deleteBookConfirm HAS BEEN CALLED");

        String nextPage = "admin/book/delete_book_ok";

        int result = bookService.deleteBookConfirm(b_no);

        if (result <= 0) {
            nextPage = "admin/book/delete_book_ng";
        }

        return nextPage;
    }

    // 대출 도서 목록
    @GetMapping("/getRentalBooks")
    public String getRentalBooks(Model model) {
        log.info("[BookController] getRentalBooks HAS BEEN CALLED");

        String nextPage = "admin/book/rental_books";

        List<RentalBookVo> rentalBookVos = bookService.getRentalBooks();

        model.addAttribute("rentalBookVos", rentalBookVos);

        return nextPage;
    }

    // 대출 도서 반납 확인
    @GetMapping("returnBookConfirm")
    public String returnBookConfirm(@RequestParam("b_no") int b_no, @RequestParam("rb_no") int rb_no) {
        log.info("[BookController] returnBookConfirm HAS BEEN CALLED");

        String nextPage = "admin/book/return_book_ok";

        int result = bookService.returnBookConfirm(b_no, rb_no);

        if (result <= 0) {
            nextPage = "admin/book/return_book_ng";
        }

        return nextPage;
    }

    // 희망 도서 목록
    @GetMapping("/getHopeBooks")
    public String getHopeBooks(Model model) {
        log.info("[BookController] getHopeBooks HAS BEEN CALLED");

        String nextPage = "admin/book/hope_books";

        List<HopeBookVo> hopeBookVos = bookService.getHopeBooks();

        model.addAttribute("hopeBookVos", hopeBookVos);

        return nextPage;
    }

    // 희망 도서 등록(입고 처리)
    @GetMapping("/registerHopeBookForm")
    public String registerHopeBookForm(Model model, HopeBookVo hopeBookVo) {
        log.info("[BookController] registerHopeBookForm HAS BEEN CALLED");

        String nextPage = "admin/book/register_hope_book_form";

        model.addAttribute("hopeBookVo", hopeBookVo);

        return nextPage;
    }

    // 희망 도서 등록(입고 처리) 확인
    @PostMapping("/registerHopeBookConfirm")
    public String registerHopeBookConfirm(BookVo bookVo,
                                          @RequestParam("hb_no") int hb_no,
                                          @RequestParam("file") MultipartFile file) {
        log.info("[BookController] registerHopeBookConfirm HAS BEEN CALLED");

        log.info("hb_no: " + hb_no);

        String nextPage = "admin/book/register_book_ok";

        // 파일 저장
        String savedFileName = uploadFileService.upload(file);

        if (savedFileName != null) {
            bookVo.setB_thumbnail(savedFileName);
            int result = bookService.registerHopeBookConfirm(bookVo, hb_no);

            if (result <= 0) {
                nextPage = "admin/book/register_book_ng";
            }
        } else {
            nextPage = "admin/book/register_book_ng";
        }

        return nextPage;
    }

    // 전체도서 목록
    @GetMapping("/getAllBooks")
    public String getAllBooks(Model model) {
        log.info("[BookController] getAllBooks HAS BEEN CALLED");

        String nextPage = "admin/book/full_list_of_books";

        List<BookVo> bookVos = bookService.getAllBooks();

        model.addAttribute("bookVos", bookVos);

        return nextPage;
    }
}
