package com.bookwise.master.record.request;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record RoleRequest(
        UUID id,
        String userRole,
        Boolean isActive,
        Set<UUID> userIds
) {
    public RoleRequest {
        if (isActive == null) isActive = true;
        if (userIds == null) userIds = new HashSet<>();
    }
}
