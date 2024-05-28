package com.ikechukwu.onlinebookstore.model.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    private String title;
    private String author;
    private String isbn;
    private int count;
}
