package com.bookwise.master.record.request;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record UserRequest(
        UUID id,
        String name,
        String email,
        String password,
        Set<String> roleName,
        String mobileNumber
) {
    public UserRequest {
        if (roleName == null) roleName = new HashSet<>();

    }
}
