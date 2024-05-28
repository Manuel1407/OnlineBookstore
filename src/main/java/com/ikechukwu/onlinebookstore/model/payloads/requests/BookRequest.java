package com.ikechukwu.onlinebookstore.model.payloads.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookRequest {

    @NotBlank(message = "Title is mandatory")
    @Size(min = 3, max = 60, message = "Title must be between 3 and 60 characters")
    private String title;

    @NotBlank(message = "Author is mandatory")
    @Size(min = 3, max = 60, message = "Author must be between 3 and 60 characters")
    private String author;

    @NotBlank(message = "ISBN is mandatory")
    @Pattern(regexp = "^[0-9-]*$", message = "ISBN must contain only numbers and hyphens")
    @Size(min = 4, max = 13, message = "ISBN must be between 4 and 13 characters")
    private String isbn;

    @NotNull(message = "Count is mandatory")
    @Min(value = 0, message = "Count must be zero or greater")
    private Integer count;

}
