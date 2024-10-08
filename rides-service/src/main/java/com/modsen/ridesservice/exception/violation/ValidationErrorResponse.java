package com.modsen.ridesservice.exception.violation;

import java.util.List;

public record ValidationErrorResponse (List<Violation> violations) {}
