package ftn.userservice.services;

import ftn.userservice.AuthPostgresIntegrationTest;
import ftn.userservice.domain.dtos.UserCreateRequest;
import ftn.userservice.domain.dtos.UserDto;
import ftn.userservice.domain.entities.Role;
import ftn.userservice.domain.entities.User;
import ftn.userservice.exception.exceptions.BadRequestException;
import ftn.userservice.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

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
        assertNotNull(retrievedUser);
        assertEquals(request.getEmail(), retrievedUser.getEmail());
    }

    @Test
    public void testLogin() {
        User loggedUser = authService.login("host", "123456");

        assertNotNull(loggedUser);
        assertEquals("host", loggedUser.getUsername());
        assertNotNull(loggedUser.getAccessToken());
    }

    @Test
    public void testInvalidLogin() {
        assertThrows(BadRequestException.class, () -> authService.login("host", "badPass"));
    }

}
