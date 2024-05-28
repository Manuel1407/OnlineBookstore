package com.ikechukwu.onlinebookstore.controller;

import com.ikechukwu.onlinebookstore.model.entity.Book;
import com.ikechukwu.onlinebookstore.model.payloads.requests.BookRequest;
import com.ikechukwu.onlinebookstore.model.payloads.responses.APIResponse;
import com.ikechukwu.onlinebookstore.service.BookService;
import com.ikechukwu.onlinebookstore.utils.Constants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public ResponseEntity<Page<Book>> viewAvailableBooks(@RequestParam(defaultValue = Constants.PAGENO) Integer pageNo,
                                                         @RequestParam(defaultValue = Constants.PAGESIZE) Integer pageSize) {
        return bookService.viewAvailableBooks(pageNo, pageSize);
    }

    @PostMapping("/addBook")
    public ResponseEntity<APIResponse> addBook(@Valid @RequestBody BookRequest requestDto){
        return bookService.addBook(requestDto);
    }

    @PutMapping("/update/{bookId}")
    public ResponseEntity<APIResponse> updateBook(@PathVariable(name = "bookId") Long id,
                                                  @Valid @RequestBody BookRequest requestDto){
        return bookService.updateBook(id, requestDto);
    }

    @DeleteMapping("/delete/{bookId}")
    public APIResponse deleteBook(@PathVariable(name = "bookId") Long bookId) {
        bookService.deleteBook(bookId);
        return new APIResponse("Book deleted successfully.", null);
    }

}