package com.ikechukwu.onlinebookstore.service;

import com.ikechukwu.onlinebookstore.model.entity.Book;
import com.ikechukwu.onlinebookstore.model.payloads.requests.BookRequest;
import com.ikechukwu.onlinebookstore.model.payloads.responses.APIResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface BookService {

    ResponseEntity<Page<Book>> viewAvailableBooks(int pageNo, int pageSize);

    ResponseEntity<APIResponse> addBook(BookRequest requestDto);

    ResponseEntity<APIResponse> updateBook(Long id, BookRequest requestDto);

    void deleteBook(Long bookId);

}
