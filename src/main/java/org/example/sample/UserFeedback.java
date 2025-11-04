package org.example.sample;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * A DTO (Data Transfer Object) to represent feedback from a user.
 * We use validation annotations to ensure the data is well-formed.
 */
public record UserFeedback(
    @NotBlank(message = "Username cannot be blank")
    String username,

    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    int rating,

    String comment
) {}
