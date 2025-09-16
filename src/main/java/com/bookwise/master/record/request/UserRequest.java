package com.bookwise.master.record.request;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record UserRequest(
        UUID id,
        String name,
        String email,
        String password,
        Set<UUID> roleId,
        String mobileNumber
) {
    public UserRequest {
        if (roleId == null) roleId = new HashSet<>();
    }
}
