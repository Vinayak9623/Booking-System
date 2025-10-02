package com.bookwise.master.record.response;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String mobileNumber,
        String email,
        String role,
        Instant createdAt,
        Instant updatedAt
) {}
