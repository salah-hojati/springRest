package org.example.sample6;

import jakarta.validation.constraints.Positive;

/**
 * A DTO representing the *optional* data that can be sent to update a book's details.
 * Note that all fields are nullable to support the PATCH semantics.
 */
public record PartialBookUpdateRequest(
    String title,
    String author,
    String isbn,
    @Positive(message = "Published year must be positive")
    Integer publishedYear,
    Double price // Price is now nullable
) {}
