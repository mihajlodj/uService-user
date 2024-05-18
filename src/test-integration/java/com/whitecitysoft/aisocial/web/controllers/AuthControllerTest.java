package com.whitecitysoft.aisocial.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whitecitysoft.aisocial.AuthPostgresIntegrationTest;
import ftn.userservice.domain.dtos.UserCreateRequest;
import ftn.userservice.domain.entities.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.contains;
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
    public void testCreateUser() throws Exception {

        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .email("newuser@example.com")
                .password("Password123")
                .firstName("New")
                .lastName("User")
                .role(Role.GUEST)
                .build();

        mockMvc.perform(post("/api/users")
                        .param("roles", "ADMIN")
                        .header("Authorization", "Bearer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.firstName").value("New"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.roles", contains("EXPERIMENTAL")));
    }
}
