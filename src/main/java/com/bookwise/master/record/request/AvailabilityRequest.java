package com.bookwise.master.record.request;

import com.bookwise.master.entity.ReservationStatus;

import java.time.Instant;
import java.util.UUID;

public record AvailabilityRequest (
        UUID resourceId,
        ReservationStatus status,
        Instant startTime,
        Instant endTime) {
}
