package com.bookwise.master.record.response;

import com.bookwise.master.entity.ReservationStatus;

import java.time.Instant;
import java.util.UUID;

public record ReservationResponse(
        UUID id,
        UUID resourceId,
        UUID userId,
        ReservationStatus status,
        Double price,
        Instant startTime,
        Instant endTime,
        Instant createdAt,
        Instant updatedAt
) {}

