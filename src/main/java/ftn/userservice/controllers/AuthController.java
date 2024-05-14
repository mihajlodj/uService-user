package ftn.userservice.controllers;

import ftn.userservice.domain.dtos.LoginRequest;
import ftn.userservice.domain.entities.User;
import ftn.userservice.services.UserService;
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

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        //User user = authService.login(request.getEmail(), request.getPassword());
        //return ResponseEntity.ok(user);
        return null; //TODO
    }

}
