package com.modsen.cabaggregatorspringbootstarter.exception.violation;

import java.util.List;

public record ValidationErrorResponse (List<Violation> violations) {}
