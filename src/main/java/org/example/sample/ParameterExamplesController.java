package org.example.sample;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * This controller demonstrates various ways to receive parameters in a REST API.
 */
@RestController
@RequestMapping("/api/parameters") // A new base path for these examples
public class ParameterExamplesController {

    /**
     * Scenario 1: Path Parameters (from the URL path)
     * Captures a dynamic value directly from the URL. Essential for identifying a specific resource.
     * How to test: GET http://localhost:8081/api/parameters/item/123
     */
    @GetMapping("/item/{itemId}")
    public String getByPathParameter(@PathVariable long itemId) {
        return "You requested the item with the Path Parameter ID: " + itemId;
    }

    /**
     * Scenario 2: Query Parameters (from the URL query string)
     * Used for optional filtering, sorting, or pagination.
     * How to test: GET http://localhost:8081/api/parameters/search?category=tech&sort=price_asc&limit=20
     */
    @GetMapping("/search")
    public String getByQueryParameters(
            @RequestParam String category, // A required query parameter
            @RequestParam(required = false) String sort, // An optional query parameter
            @RequestParam(defaultValue = "10") int limit // An optional parameter with a default value
    ) {
        return String.format(
            "Searching in category '%s'. Sorting by '%s'. Limit is %d.",
            category,
            sort != null ? sort : "default", // Handle the optional value
            limit
        );
    }

    /**
     * Scenario 3: Request Body (the data payload of the request)
     * Used to send complex data, like a JSON object, when creating or updating a resource.
     * How to test: POST http://localhost:8081/api/parameters/feedback with a JSON body.
     */
    @PostMapping("/feedback")
    public ResponseEntity<String> processRequestBody(@Valid @RequestBody UserFeedback feedback) {
        String responseMessage = String.format(
            "Received feedback from '%s' with rating %d. Comment: '%s'",
            feedback.username(),
            feedback.rating(),
            feedback.comment()
        );
        return ResponseEntity.ok(responseMessage);
    }

    /**
     * Scenario 4: Header Parameters (metadata about the request)
     * Used for sending metadata like authentication tokens, client type, or caching controls.
     * How to test: GET http://localhost:8081/api/parameters/headers and add a custom header "X-Client-Version".
     */
    @GetMapping("/headers")
    public String getFromHeaders(
            @RequestHeader("User-Agent") String userAgent,
            @RequestHeader(name = "X-Client-Version", required = false, defaultValue = "unknown") String clientVersion
    ) {
        return String.format(
            "Request came from client version '%s'. Full User-Agent: '%s'",
            clientVersion,
            userAgent
        );
    }

    /**
     * Bonus Scenario: Capturing all headers
     * Sometimes you need to inspect all headers without knowing their names in advance.
     */
    @GetMapping("/all-headers")
    public ResponseEntity<Map<String, String>> getAllHeaders(@RequestHeader Map<String, String> headers) {
        // The map will contain all request headers.
        // You can now iterate over them or look for specific ones.
        return ResponseEntity.ok(headers);
    }
}
