package com.modsen.ratingservice.exception.violation;

import java.util.List;

public record ValidationErrorResponse (List<Violation> violations) {}
