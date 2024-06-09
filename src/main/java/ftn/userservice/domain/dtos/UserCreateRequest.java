package ftn.userservice.domain.dtos;

import ftn.userservice.domain.entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    @NotEmpty(message = "Username is required")
    private String username;
    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotEmpty(message = "Password is required")
    private String password;
    @NotEmpty(message = "Password is required")
    private String repeatPassword;
    @NotEmpty(message = "First name is required")
    private String firstName;
    @NotEmpty(message = "Last name is required")
    private String lastName;
    @Builder.Default
    @NotNull(message = "Role is required")
    private Role role = Role.GUEST;

}
