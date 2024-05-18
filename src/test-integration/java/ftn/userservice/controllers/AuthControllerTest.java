package ftn.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ftn.userservice.AuthPostgresIntegrationTest;
import ftn.userservice.domain.dtos.LoginRequest;
import ftn.userservice.domain.dtos.UserCreateRequest;
import ftn.userservice.domain.entities.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Sql("/sql/auth.sql")
public class AuthControllerTest extends AuthPostgresIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegister() throws Exception {
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .username("newUser")
                .email("newuser@ftn.com")
                .password("pass123")
                .repeatPassword("pass123")
                .firstName("New")
                .lastName("User")
                .role(Role.GUEST)
                .build();

        mockMvc.perform(post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("newuser@ftn.com"))
                .andExpect(jsonPath("$.username").value("newUser"))
                .andExpect(jsonPath("$.firstName").value("New"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.role").value("GUEST"));
    }

    @Test
    public void testLogin() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .username("guest")
                .password("123456")
                .build();

        mockMvc.perform(post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("guest"))
                .andExpect(jsonPath("$.accessToken").exists());
    }

}
