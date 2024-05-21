package ftn.userservice.services;

import ftn.userservice.domain.dtos.UserDto;
import ftn.userservice.domain.entities.Role;
import ftn.userservice.domain.entities.User;
import ftn.userservice.exception.exceptions.NotFoundException;
import ftn.userservice.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void testGetByIdUserExists() {
        UUID userId = UUID.randomUUID();
        User user = createUser(userId);
        UserDto userDto = createUserDto(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserDto resultDto = userService.getById(userId);

        assertNotNull(resultDto);
        assertEquals(userDto.getId(), resultDto.getId());
        assertEquals(userDto.getUsername(), resultDto.getUsername());
        assertEquals(userDto.getEmail(), resultDto.getEmail());
        assertEquals(userDto.getFirstName(), resultDto.getFirstName());
        assertEquals(userDto.getLastName(), resultDto.getLastName());
        assertEquals(userDto.getRole(), resultDto.getRole());

        verify(userRepository).findById(userId);
    }

    @Test
    void testGetByIdUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> userService.getById(userId));

        Assertions.assertTrue(thrown.getMessage().contains("User doesn't exist"));
        verify(userRepository).findById(userId);
    }

    private User createUser(UUID userId) {
        return User.builder()
                .id(userId)
                .username("testUser")
                .email("test@example.com")
                .password("securePassword")
                .firstName("Test")
                .lastName("User")
                .role(Role.GUEST)
                .build();
    }

    private UserDto createUserDto(UUID userId) {
        return UserDto.builder()
                .id(userId)
                .username("testUser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .role(Role.GUEST)
                .build();
    }

}
