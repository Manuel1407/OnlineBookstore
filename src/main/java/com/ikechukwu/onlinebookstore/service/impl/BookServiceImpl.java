package com.ikechukwu.onlinebookstore.service.impl;

import com.ikechukwu.onlinebookstore.model.entity.Book;
import com.ikechukwu.onlinebookstore.model.enums.Status;
import com.ikechukwu.onlinebookstore.exceptions.ResourceNotFoundException;
import com.ikechukwu.onlinebookstore.model.payloads.requests.BookRequest;
import com.ikechukwu.onlinebookstore.model.payloads.responses.APIResponse;
import com.ikechukwu.onlinebookstore.model.payloads.responses.BookResponse;
import com.ikechukwu.onlinebookstore.repository.BookRepository;
import com.ikechukwu.onlinebookstore.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<Page<Book>> viewAvailableBooks(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        // Query for available books with a count greater than or equal to 1 with pagination
        Page<Book> pagedResult = bookRepository.findByCountGreaterThanEqual(1, paging);
        if(pagedResult.hasContent()) {
            log.info("Books found.");
            return new ResponseEntity<>(pagedResult, HttpStatus.OK);
        } else {
            log.error("No books found.");
            throw new ResourceNotFoundException("No books found.");
        }
    }

    @Override
    public ResponseEntity<APIResponse> addBook(BookRequest requestDto) {
        // Check if a book with the same title and author already exists
        Book book = bookRepository.findByTitle(requestDto.getTitle()).orElse(null);
        if (book != null && book.getAuthor().equalsIgnoreCase(requestDto.getAuthor())) {
            log.error("Book already exists.");
            return new ResponseEntity<>(new APIResponse("Book already exists.", null),
                    HttpStatus.NOT_ACCEPTABLE);
        }
        // Build and save the new book object
        Book newBook = Book.builder()
                .title(requestDto.getTitle())
                .author(requestDto.getAuthor())
                .isbn(requestDto.getIsbn())
                .count(requestDto.getCount())
                .status(requestDto.getCount() > 0 ? Status.AVAILABLE : Status.UNAVAILABLE)
                .build();
        bookRepository.save(newBook);
        BookResponse bookResponse = modelMapper.map(newBook, BookResponse.class);
        log.info("Book added successfully");
        return new ResponseEntity<>(new APIResponse("Book added successfully", bookResponse),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<APIResponse> updateBook(Long id, BookRequest requestDto) {
        // Find the book by ID or throw an exception if not found
        Book book = bookRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Book not found."));
        // Update and save the book's details
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setCount(requestDto.getCount());
        book.setStatus(requestDto.getCount() > 0 ? Status.AVAILABLE : Status.UNAVAILABLE);
        bookRepository.save(book);
        BookResponse bookResponse = modelMapper.map(book, BookResponse.class);
        log.info("Book with ID {} updated successfully", id);
        return new ResponseEntity<>(new APIResponse("Updated successfully", bookResponse), HttpStatus.OK);
    }

    @Override
    public void deleteBook(Long bookId) {
        // Check if the book exists by ID and throw an exception if not found
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Book with ID " + bookId + " not found");
        }
        // Delete the book
        bookRepository.deleteById(bookId);
        log.info("Book with ID {} deleted successfully", bookId);
    }
}
