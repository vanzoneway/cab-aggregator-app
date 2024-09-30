package com.modsen.driverservice.exception.violation;

import java.util.List;

public record ValidationErrorResponse (List<Violation> violations) {}
