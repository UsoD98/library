package com.office.library.book.admin;

import com.office.library.book.BookVo;
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
@Controller
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
}
