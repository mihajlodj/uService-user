package ftn.userservice.domain.dtos;

import ftn.userservice.domain.entities.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    @NotEmpty
    private String username;
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String repeatPassword;
    private String firstName;
    private String lastName;
    @Builder.Default
    @NotEmpty
    private Role role = Role.GUEST;

}
