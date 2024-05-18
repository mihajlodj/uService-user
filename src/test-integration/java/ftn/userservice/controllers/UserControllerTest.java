package ftn.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ftn.userservice.AuthPostgresIntegrationTest;
import ftn.userservice.domain.dtos.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Sql("/sql/auth.sql")
public class UserControllerTest extends AuthPostgresIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        authenticateGuest();
    }

    @Test
    public void testGetMe() throws Exception {
        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.username").value("guest"));
    }

    @Test
    public void testUpdate() throws Exception {
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .username("guest")
                .email("updated@ftn.com")
                .firstName("Updated")
                .lastName("User")
                .build();

        mockMvc.perform(put("/api/users")
                        .header("Authorization", "Bearer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("updated@ftn.com"))
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

}
