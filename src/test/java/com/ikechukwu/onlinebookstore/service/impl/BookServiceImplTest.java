package com.ikechukwu.onlinebookstore.service.impl;

import com.ikechukwu.onlinebookstore.model.entity.Book;
import com.ikechukwu.onlinebookstore.model.enums.Status;
import com.ikechukwu.onlinebookstore.exceptions.ResourceNotFoundException;
import com.ikechukwu.onlinebookstore.model.payloads.requests.BookRequest;
import com.ikechukwu.onlinebookstore.model.payloads.responses.APIResponse;
import com.ikechukwu.onlinebookstore.model.payloads.responses.BookResponse;
import com.ikechukwu.onlinebookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookRequest bookRequest;
    private BookResponse bookResponse;
    private Page<Book> pagedBooks;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book(1L, "Half of a Yellow Sun", "Ikechukwu Iwunna", "1234567890", 5, Status.AVAILABLE);
        bookRequest = new BookRequest();
        bookRequest.setTitle("Half of a Yellow Sun");
        bookRequest.setAuthor("Ikechukwu Iwunna");
        bookRequest.setIsbn("1234567890");
        bookRequest.setCount(5);

        bookResponse = new BookResponse("Half of a Yellow Sun", "Ikechukwu Iwunna", "1234567890", 5);
        pagedBooks = new PageImpl<>(Collections.singletonList(book));
    }

    @Test
    void viewAvailableBooks_ReturnsPagedBooks() {
        when(bookRepository.findByCountGreaterThanEqual(anyInt(), any(PageRequest.class))).thenReturn(pagedBooks);
        ResponseEntity<Page<Book>> response = bookService.viewAvailableBooks(0, 10);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).hasContent());
        verify(bookRepository, times(1)).findByCountGreaterThanEqual(anyInt(), any(PageRequest.class));
    }

    @Test
    void viewAvailableBooks_ReturnsNotFoundWhenNoBooksAvailable() {
        when(bookRepository.findByCountGreaterThanEqual(anyInt(), any(PageRequest.class))).thenReturn(Page.empty());
        ResponseEntity<Page<Book>> response = bookService.viewAvailableBooks(0, 10);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void addBook_BookAlreadyExists() {
        when(bookRepository.findByTitle(anyString())).thenReturn(Optional.of(book));
        ResponseEntity<APIResponse> response = bookService.addBook(bookRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertEquals("Book already exists.", response.getBody().getMessage());
        verify(bookRepository, times(1)).findByTitle(anyString());
    }

    @Test
    void addBook_AddsNewBookSuccessfully() {
        when(bookRepository.findByTitle(anyString())).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(modelMapper.map(any(Book.class), eq(BookResponse.class))).thenReturn(bookResponse);
        ResponseEntity<APIResponse> response = bookService.addBook(bookRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Book added successfully", response.getBody().getMessage());
    }

    @Test
    void updateBook_BookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.updateBook(1L, bookRequest);
        });
        assertEquals("Book not found.", exception.getMessage());
    }

    @Test
    void updateBook_UpdatesBookSuccessfully() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(modelMapper.map(any(Book.class), eq(BookResponse.class))).thenReturn(bookResponse);
        ResponseEntity<APIResponse> response = bookService.updateBook(1L, bookRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated successfully", response.getBody().getMessage());
    }

    @Test
    void deleteBook_BookNotFound() {
        when(bookRepository.existsById(anyLong())).thenReturn(false);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.deleteBook(1L);
        });
        assertEquals("Book with ID 1 not found", exception.getMessage());
        verify(bookRepository, times(1)).existsById(anyLong());
    }

    @Test
    void deleteBook_DeletesBookSuccessfully() {
        when(bookRepository.existsById(anyLong())).thenReturn(true);
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).existsById(anyLong());
        verify(bookRepository, times(1)).deleteById(anyLong());
    }
}