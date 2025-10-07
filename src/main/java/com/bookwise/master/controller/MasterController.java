package com.bookwise.master.controller;

import com.bookwise.master.record.request.*;
import com.bookwise.master.service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/master")
public class MasterController {

    @Autowired
    private MasterService service;

    @PostMapping("/role")
    public ResponseEntity<?> createRole(@RequestBody RoleRequest request,@RequestHeader("Authorization") String authHeader){
        return service.createRole(request,authHeader);
    }

    @PostMapping("/registerUser")
    public ResponseEntity<?> createUser(@RequestBody UserRequest request,@RequestHeader("Authorization") String authHeader){
        return service.createUser(request,authHeader);
    }


    @GetMapping("/getUsers")
    public ResponseEntity<?> getUserList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5")
    int size, @RequestParam(defaultValue = "name") String sortfield, @RequestParam(defaultValue = "asc") String sortDir) {
        return service.getUserList(page, size, sortfield, sortDir);
    }

    @PostMapping("/createResource")
    public ResponseEntity<?> createResource(@RequestBody ResourceRequest request) {
        return service.createOrUpdateResource(request);
    }

    @GetMapping("/getResource/{id}")
    public ResponseEntity<?> getResourceById(@PathVariable("id") UUID id) {
        return service.getResourceById(id);
    }

    @GetMapping("/getResources")
    public ResponseEntity<?> getResourceList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5")
    int size, @RequestParam(defaultValue = "name") String sortfield, @RequestParam(defaultValue = "asc") String sortDir) {
        return service.getResourceList(page, size, sortfield, sortDir);
    }

    @DeleteMapping("/deleteResource/{id}")
    public ResponseEntity<?> deleteResource(@PathVariable("id") UUID id) {
        return service.deleteResource(id);
    }

    @PostMapping("/createReservation")
    public ResponseEntity<?> createorUpdateReservation(@RequestBody ReservationRequest request) {
        return service.createOrUpdateReservation(request);
    }

    @GetMapping("/reservation/{id}")
    public ResponseEntity<?> getreservationbyId(@PathVariable("id") UUID id) {
        return service.getReservationById(id);
    }

    @GetMapping("/getReservations")
    public ResponseEntity<?> getReservationList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5")
    int size, @RequestParam(defaultValue = "startTime") String sortfield, @RequestParam(defaultValue = "asc") String sortDir) {
        return service.getReservationList(page, size, sortfield, sortDir);

    }

    @PostMapping("/checkAvailability")
    public ResponseEntity<?> checkAvailablity(@RequestBody AvailabilityRequest  request){
        return service.checkAvailabilityForReservation(request);
    }

    @DeleteMapping("/deleteReservation/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable("id") UUID id) {
        return service.deleteReservation(id);
    }

}
