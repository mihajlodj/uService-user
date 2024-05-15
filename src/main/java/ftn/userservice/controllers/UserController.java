package ftn.userservice.controllers;

import ftn.userservice.domain.dtos.ChangePasswordRequest;
import ftn.userservice.domain.dtos.UserCreateRequest;
import ftn.userservice.domain.dtos.UserUpdateRequest;
import ftn.userservice.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        return ResponseEntity.ok(userService.me());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid UserCreateRequest userCreateRequest) {
        return ResponseEntity.ok(userService.create(userCreateRequest));
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        return ResponseEntity.ok(userService.update(userUpdateRequest));
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> update(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);
        return ResponseEntity.ok().build();
    }

}
