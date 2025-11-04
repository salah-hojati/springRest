package org.example.sample5;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * A DTO representing the complete data needed to update a book.
 * Includes validation to ensure all fields are present and valid.
 */
public record UpdateBookRequest(
    @NotBlank(message = "Title cannot be blank")
    String title,

    @NotBlank(message = "Author cannot be blank")
    String author,

    @NotBlank(message = "ISBN cannot be blank")
    String isbn,

    @NotNull(message = "Published year cannot be null")
    @Positive(message = "Published year must be a positive number")
    Integer publishedYear
) {}
