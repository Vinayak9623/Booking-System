package com.bookwise.master.record.request;

import com.bookwise.master.entity.ReservationStatus;

import java.time.Instant;
import java.util.UUID;

public record ReservationRequest(
        UUID id,
        UUID resourceId,
        UUID userId,
        ReservationStatus status,
        Double price,
        Instant startTime,
        Instant endTime
) {}