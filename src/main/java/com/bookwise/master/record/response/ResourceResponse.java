package com.bookwise.master.record.response;

import java.time.Instant;
import java.util.UUID;

public record ResourceResponse(
        UUID id,
        String name,
        String type,
        String description,
        Integer capacity,
        Boolean active,
        Instant createdAt,
        Instant updatedAt
) {}