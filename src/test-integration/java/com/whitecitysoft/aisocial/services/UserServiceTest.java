package com.whitecitysoft.aisocial.services;

import com.whitecitysoft.aisocial.AuthPostgresIntegrationTest;
import ftn.userservice.domain.dtos.ChangePasswordRequest;
import ftn.userservice.domain.dtos.UserCreateRequest;
import ftn.userservice.domain.dtos.UserDto;
import ftn.userservice.domain.dtos.UserUpdateRequest;
import ftn.userservice.domain.entities.Role;
import ftn.userservice.domain.entities.User;
import ftn.userservice.exception.exceptions.NotFoundException;
import ftn.userservice.repositories.UserRepository;
import ftn.userservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Sql("/sql/auth.sql")
public class UserServiceTest extends AuthPostgresIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        authenticateGuest();
    }

    @Test
    public void testMe() {
        UserDto expectedUserDto = userService.getById(UUID.fromString("e49fcaa5-d45b-4556-9d91-13e58187fea6"));
        UserDto actualUserDto = userService.me();

        assertNotNull(expectedUserDto);
        assertNotNull(actualUserDto);
        assertEquals(expectedUserDto.getFirstName(), actualUserDto.getFirstName());
        assertEquals(expectedUserDto.getLastName(), actualUserDto.getLastName());
    }

    @Test
    public void testGetById() {
        UUID existingUserId = UUID.fromString("e49fcaa5-d45b-4556-9d91-13e58187fea6");

        UserDto retrievedDto = userService.getById(existingUserId);
        assertNotNull(retrievedDto);
        assertEquals("Guest", retrievedDto.getFirstName());
        assertEquals("Guest", retrievedDto.getLastName());

        // Test retrieval of a non-existing user
        UUID nonExistingUserId = UUID.randomUUID();
        assertThrows(NotFoundException.class, () -> userService.getById(nonExistingUserId));
    }

    @Test
    public void testUpdateUser() {
        UserUpdateRequest request = UserUpdateRequest.builder()
                .email("test@test.rs")
                .firstName("Updated")
                .lastName("Updated")
                .build();

        UserDto updatedUser = userService.update(request);

        assertNotNull(updatedUser);
        assertEquals(request.getFirstName(), updatedUser.getFirstName());
        assertEquals(request.getLastName(), updatedUser.getLastName());

        User retrievedUser = userRepository.findById(UUID.fromString("e49fcaa5-d45b-4556-9d91-13e58187fea6")).orElse(null);
        assertNotNull(retrievedUser);
        assertEquals(request.getFirstName(), retrievedUser.getFirstName());
    }

    @Test
    public void changePasswordTest() {
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .oldPassword("123456")
                .newPassword("new123")
                .repeatNewPassword("new123")
                .build();

        userService.changePassword(request);

        User retrievedUser = userRepository.findById(UUID.fromString("e49fcaa5-d45b-4556-9d91-13e58187fea6")).orElse(null);
        assertNotNull(retrievedUser);
        assertTrue(passwordEncoder.matches("new123", retrievedUser.getPassword()));
    }

}
