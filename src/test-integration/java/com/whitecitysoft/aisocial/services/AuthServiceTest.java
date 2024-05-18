package com.whitecitysoft.aisocial.services;

import com.whitecitysoft.aisocial.AuthPostgresIntegrationTest;
import ftn.userservice.domain.dtos.UserCreateRequest;
import ftn.userservice.domain.dtos.UserDto;
import ftn.userservice.domain.entities.Role;
import ftn.userservice.domain.entities.User;
import ftn.userservice.repositories.UserRepository;
import ftn.userservice.services.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Sql("/sql/auth.sql")
public class AuthServiceTest extends AuthPostgresIntegrationTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testRegister() {
        UserCreateRequest request = UserCreateRequest.builder()
                .username("testUser")
                .email("testuser@example.com")
                .password("123456")
                .repeatPassword("123456")
                .firstName("Test")
                .lastName("User")
                .role(Role.GUEST)
                .build();

        UserDto createdUser = authService.create(request);

        assertNotNull(createdUser);
        assertEquals(request.getEmail(), createdUser.getEmail());
        assertEquals(request.getUsername(), createdUser.getUsername());
        assertEquals(request.getFirstName(), createdUser.getFirstName());
        assertEquals(request.getLastName(), createdUser.getLastName());
        assertEquals(request.getRole(), createdUser.getRole());

        User retrievedUser = userRepository.findByUsername(request.getUsername()).orElse(null);
        System.out.println(retrievedUser.getPassword());
        assertNotNull(retrievedUser);
        assertEquals(request.getEmail(), retrievedUser.getEmail());
    }

    public void testLogin() {
        //TODO
    }

}
