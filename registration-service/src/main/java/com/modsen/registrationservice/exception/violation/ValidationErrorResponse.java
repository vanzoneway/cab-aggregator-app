package com.modsen.registrationservice.exception.violation;

import java.util.List;

public record ValidationErrorResponse (List<Violation> violations) {}
