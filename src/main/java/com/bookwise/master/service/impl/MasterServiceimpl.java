package com.bookwise.master.service.impl;

import com.bookwise.common.ApiResponse;
import com.bookwise.common.PaginatedResult;
import com.bookwise.master.entity.Reservation;
import com.bookwise.master.entity.Resource;
import com.bookwise.master.entity.Role;
import com.bookwise.master.entity.User;
import com.bookwise.master.exception.customeEx.ReservationNotFound;
import com.bookwise.master.exception.customeEx.ResourceNotFound;
import com.bookwise.master.record.request.ReservationRequest;
import com.bookwise.master.record.request.ResourceRequest;
import com.bookwise.master.record.request.RoleRequest;
import com.bookwise.master.record.request.UserRequest;
import com.bookwise.master.record.response.ReservationResponse;
import com.bookwise.master.record.response.ResourceResponse;
import com.bookwise.master.record.response.UserResponse;
import com.bookwise.master.repository.ReservationRepository;
import com.bookwise.master.repository.ResourceRepository;
import com.bookwise.master.repository.RoleRepository;
import com.bookwise.master.repository.UserRepository;
import com.bookwise.master.service.MasterService;
import com.bookwise.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MasterServiceimpl implements MasterService {

    private final ResourceRepository resourceRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<?> createRole(RoleRequest request, String authHeader) {
        var response=new ApiResponse<>();
        String token=authHeader.replace("Bearer ","");
        List<String> role1 = jwtService.getRoleFromToken(token);
        try {
            if(!role1.contains("ADMIN")){
               throw new AccessDeniedException("Only ADMIN can acces this service");
            }
        }catch (Exception e){
            response.responseMethod(HttpStatus.UNAUTHORIZED.value(), "You are not autjorzied",null,null);
           return ResponseEntity.ok(response);
        }
        Role role;
        role=Objects.nonNull(request.id())?
                roleRepository.findByUserRole(request.userRole()).orElseThrow(()-> new RuntimeException("Role not found with given Id"+request.id()))
                :new Role();
        role.setUserRole(request.userRole());
        role.setIsActive(request.isActive());
        if(!request.userIds().isEmpty()){
            List<User> users =request.userIds()
                    .stream()
                    .map(user->userRepository
                            .findById(user)
                            .orElseThrow(()->new RuntimeException("User not found with given Id")))
                    .collect(Collectors.toList());
            role.setUsers(users);
        }
        if(Objects.isNull(request.id())){
            response.responseMethod(HttpStatus.CREATED.value(), "Role created successfully",null,null);
        }
        else {
            response.responseMethod(HttpStatus.OK.value(), "Role updated successfully",null,null);
        }
        roleRepository.save(role);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> createUser(UserRequest request,String authHeader) {
        var response=new ApiResponse<>();
        String token=authHeader.replace("Bearer ","");
        String email = jwtService.extractUsername(token);
        Set<Role> role1 = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")).getRoles();

        boolean isAdmin = role1.stream().anyMatch(x -> "ADMIN".equalsIgnoreCase(x.getUserRole()));
        if (!isAdmin) {
            response.responseMethod(HttpStatus.UNAUTHORIZED.value(), "Access denied for user role", null, null);
            return ResponseEntity.ok(response);
        }

        User user;
        if(request.id()==null){
            user = new User();
            user.setName(request.name());
            user.setEmail(request.email());
            user.setPassword(passwordEncoder.encode(request.password()));

            Set<Role> set=new HashSet<>();
            if(request.roleName()!=null && !request.roleName().isEmpty()){
                Set<Role> roles=request.roleName().stream()
                        .map(role->roleRepository
                                .findByUserRole(role).orElseThrow(()->new RuntimeException("Role not found with given Id")))
                        .collect(Collectors.toSet());
                user.setRoles(roles);
            }
            else {
                String defaultUserRole="USER";
                if("vinayak@gmail.com".equalsIgnoreCase(request.email())){
                    defaultUserRole="ADMIN";
                }

                Role role = roleRepository.findByUserRole(defaultUserRole).orElseThrow(() -> new RuntimeException("Role not found "));
                set.add(role);
                user.setRoles(set);
            }
            user.setMobileNumber(request.mobileNumber());
            response.responseMethod(HttpStatus.CREATED.value(), "User creted successfully",null,null);
        }
        else{
            UUID id = request.id();
            user=userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found with given Id"));
            user.setName(request.name());
            user.setEmail(request.email());
            user.setPassword(passwordEncoder.encode(request.password()));
            Set<Role> roles =request.roleName().stream().map(role->roleRepository.findByUserRole(role)
                            .orElseThrow(()->new RuntimeException("Role not found with given Id")))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
            user.setMobileNumber(request.mobileNumber());
            response.responseMethod(HttpStatus.OK.value(), "User updated successfully",null,null);

        }
        userRepository.save(user);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getUserList(int page, int size, String sortfield, String sortDir) {
        var response = new ApiResponse<>();

        try {
            // Sorting
            Sort sort = sortDir.equalsIgnoreCase("asc")
                    ? Sort.by(sortfield).ascending()
                    : Sort.by(sortfield).descending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<User> userPage = userRepository.findAll(pageable);

            // Map User -> UserResponse
            List<UserResponse> responseList = userPage.stream()
                    .map(user -> {
                        // Get first role name or empty string if no roles
                        String roleName = user.getRoles().stream()
                                .findFirst()
                                .map(Role::getUserRole)
                                .orElse("");

                        return new UserResponse(
                                user.getId(),
                                user.getName(),
                                user.getMobileNumber(),
                                user.getEmail(),
                                roleName,
                                user.getCreatedAt(),
                                user.getUpdatedAt()
                        );
                    })
                    .toList();

            // Build paginated result
            var result = new PaginatedResult<>(
                    responseList,
                    userPage.getNumber(),
                    userPage.getSize(),
                    userPage.getTotalElements(),
                    userPage.getTotalPages(),
                    userPage.isLast()
            );

            // Set API response
            response.responseMethod(
                    HttpStatus.OK.value(),
                    "Resources fetched successfully",
                    result,
                    null
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace(); // optional: log the exception
            response.responseMethod(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to fetch users",
                    null,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @Override
    public ResponseEntity<?> createOrUpdateResource(ResourceRequest request) {
        var response = new ApiResponse<>();
        Resource resource;
        resource = Objects.nonNull(request.id()) ?
                resourceRepository.findById(request.id())
                        .orElseThrow(() -> new ResourceNotFound("Resource not found"))
                : new Resource();
        resource.setName(request.name());
        resource.setType(request.type());
        resource.setDescription(request.description());
        resource.setCapacity(request.capacity());
        resource.setActive(request.active() != null ? request.active() : true);
        Resource save = resourceRepository.save(resource);
        ResourceResponse resourceResponse = new ResourceResponse(
                save.getId(),
                save.getName(),
                save.getType(),
                save.getDescription(),
                save.getCapacity(),
                save.isActive(),
                save.getCreatedAt(),
                save.getUpdatedAt()
        );
        String message = Objects.isNull(request.id()) ? "Resource created successfully " : "Resource updated successfully";
        response.responseMethod(HttpStatus.CREATED.value(), message, resourceResponse, null);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getResourceById(UUID id) {
        var response = new ApiResponse<>();
        Resource resource = resourceRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFound("Resource not found with given ID: " + id));

        ResourceResponse resourceResponse = new ResourceResponse(
                resource.getId(),
                resource.getName(),
                resource.getType(),
                resource.getDescription(),
                resource.getCapacity(),
                resource.isActive(),
                resource.getCreatedAt(),
                resource.getUpdatedAt()
        );
        response.responseMethod(HttpStatus.OK.value(), "resource fetch successfully", resourceResponse, null);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getResourceList(int page, int size, String sortfield, String sortDir) {
        var response = new ApiResponse<>();

        try {
            Sort sort = sortDir.equalsIgnoreCase("asc")
                    ? Sort.by(sortfield).ascending()
                    : Sort.by(sortfield).descending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Resource> resourcePage = resourceRepository.findAll(pageable);

            List<ResourceResponse> responseList = resourcePage.stream()
                    .map(resource -> new ResourceResponse(
                            resource.getId(),
                            resource.getName(),
                            resource.getType(),
                            resource.getDescription(),
                            resource.getCapacity(),
                            resource.isActive(),
                            resource.getCreatedAt(),
                            resource.getUpdatedAt()
                    ))
                    .toList();

            var result = new PaginatedResult<>(
                    responseList,
                    resourcePage.getNumber(),
                    resourcePage.getSize(),
                    resourcePage.getTotalElements(),
                    resourcePage.getTotalPages(),
                    resourcePage.isLast()
            );

            response.responseMethod(
                    HttpStatus.OK.value(),
                    "Resources fetched successfully",
                    result,
                    null
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.responseMethod(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to fetch resources",
                    null,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<?> deleteResource(UUID id) {
        var response = new ApiResponse<>();
        resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFound("resource not found"));
        resourceRepository.deleteById(id);
        response.responseMethod(HttpStatus.OK.value(), "resource deleted succefully", null, null);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> createOrUpdateReservation(ReservationRequest request) {
        var response = new ApiResponse<>();
        Reservation reservation;

        reservation = request.id() != null
                ? reservationRepository.findById(request.id())
                .orElseThrow(() -> new ReservationNotFound("Reservation not found"))
                : new Reservation();

        Resource resource = resourceRepository.findById(request.resourceId())
                .orElseThrow(() -> new ReservationNotFound("Reservation not found"));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        reservation.setResource(resource);
        reservation.setUser(user);
        reservation.setStatus(request.status());
        reservation.setPrice(request.price());
        reservation.setStartTime(request.startTime());
        reservation.setEndTime(request.endTime());

        Reservation saved = reservationRepository.save(reservation);

        ReservationResponse reservationResponse = new ReservationResponse(
                saved.getId(),
                saved.getResource().getId(),
                saved.getUser().getId(),
                saved.getStatus(),
                saved.getPrice(),
                saved.getStartTime(),
                saved.getEndTime(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
        String message = request.id() == null ? "Reservation created successfully" : "Reservation updated successfully";
        response.responseMethod(HttpStatus.CREATED.value(), message, reservationResponse, null);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getReservationById(UUID id) {
        var response = new ApiResponse<>();
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFound("Reservation not found with ID: " + id));
        ReservationResponse reservationResponse = new ReservationResponse(
                reservation.getId(),
                reservation.getResource().getId(),
                reservation.getUser().getId(),
                reservation.getStatus(),
                reservation.getPrice(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt()
        );
        response.responseMethod(HttpStatus.OK.value(), "Fetch reservation successfully", reservationResponse, null);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getReservationList(int page, int size, String sortField, String sortDir) {
        var response = new ApiResponse<>();
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Reservation> reservationPage = reservationRepository.findAll(pageable);

        List<ReservationResponse> responseList = reservationPage.stream()
                .map(res -> new ReservationResponse(
                        res.getId(),
                        res.getResource().getId(),
                        res.getUser().getId(),
                        res.getStatus(),
                        res.getPrice(),
                        res.getStartTime(),
                        res.getEndTime(),
                        res.getCreatedAt(),
                        res.getUpdatedAt()
                ))
                .toList();

        var result = new PaginatedResult<>(
                responseList,
                reservationPage.getNumber(),
                reservationPage.getSize(),
                reservationPage.getTotalElements(),
                reservationPage.getTotalPages(),
                reservationPage.isLast()
        );

        response.responseMethod(HttpStatus.OK.value(), "Reservations fetched successfully", result, null);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> deleteReservation(UUID id) {
        var response = new ApiResponse<>();
        reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFound("Reservation not found"));
        reservationRepository.deleteById(id);
        response.responseMethod(HttpStatus.OK.value(), "Reservation deleted succefully", null, null);
        return ResponseEntity.ok(response);
    }


}
