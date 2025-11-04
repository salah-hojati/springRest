package org.example.sample4;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NewBookRequest(
    @NotBlank(message = "Title cannot be blank")
    String title,
    @NotBlank(message = "Author cannot be blank")
    String author,
    @NotBlank(message = "ISBN cannot be blank")
    String isbn,
    @NotNull(message = "Published year cannot be null")
    @Positive(message = "Published year must be positive")
    Integer publishedYear
) {}
