package com.office.library.book.user;

import com.office.library.book.BookVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
