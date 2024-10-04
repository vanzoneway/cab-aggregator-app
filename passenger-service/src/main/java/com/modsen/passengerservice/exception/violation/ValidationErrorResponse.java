package com.modsen.passengerservice.exception.violation;

import java.util.List;

public record ValidationErrorResponse (List<Violation> violations) {}
