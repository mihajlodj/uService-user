package ftn.userservice.controllers;

import ftn.userservice.domain.dtos.LoginRequest;
import ftn.userservice.domain.dtos.UserCreateRequest;
import ftn.userservice.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody @Valid UserCreateRequest userCreateRequest) {
        return ResponseEntity.ok(authService.create(userCreateRequest));
    }

}
