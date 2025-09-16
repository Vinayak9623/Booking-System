package com.bookwise.security.controller;

import com.bookwise.common.ApiResponse;
import com.bookwise.master.entity.User;
import com.bookwise.master.repository.UserRepository;
import com.bookwise.security.JwtService;
import com.bookwise.security.dto.AuthRequest;
import com.bookwise.security.dto.Authresponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Authresponse>> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String jwtToken = jwtService.generateToken(user);
            Authresponse authresponse = Authresponse.builder()
                    .token(jwtToken)
                    .build();

            ApiResponse<Authresponse> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Login successful",
                    authresponse,
                    null
            );

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            ApiResponse<Authresponse> errorResponse = new ApiResponse<>(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Invalid credentials",
                    null,
                    null
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}
