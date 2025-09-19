package com.bookwise.master.service;

import com.bookwise.master.record.request.ReservationRequest;
import com.bookwise.master.record.request.ResourceRequest;
import com.bookwise.master.record.request.RoleRequest;
import com.bookwise.master.record.request.UserRequest;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface MasterService {
    public ResponseEntity<?> createRole(RoleRequest request, String authHeader);
    ResponseEntity<?> createUser(UserRequest request,String authHeader);
    ResponseEntity<?> createOrUpdateResource(ResourceRequest request);
    ResponseEntity<?> getResourceById(UUID id);
    ResponseEntity<?> getResourceList(int page ,int size,String sortfield,String sortDir);
    ResponseEntity<?> deleteResource(UUID id);
    ResponseEntity<?> createOrUpdateReservation(ReservationRequest request);
    ResponseEntity<?> getReservationById(UUID id);
    ResponseEntity<?> getReservationList(int page, int size, String sortField, String sortDir);
    ResponseEntity<?> deleteReservation(UUID id);
}
