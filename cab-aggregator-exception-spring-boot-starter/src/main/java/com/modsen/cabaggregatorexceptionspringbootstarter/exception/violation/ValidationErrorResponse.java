package com.modsen.cabaggregatorexceptionspringbootstarter.exception.violation;

import java.util.List;

public record ValidationErrorResponse(List<Violation> violations) {
}
