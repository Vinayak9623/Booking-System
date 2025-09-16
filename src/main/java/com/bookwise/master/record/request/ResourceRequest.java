package com.bookwise.master.record.request;

import java.util.UUID;

public record ResourceRequest(
        UUID id,
        String name,
        String type,
        String description,
        Integer capacity,
        Boolean active
) {}
