package com.whitecitysoft.aisocial.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whitecitysoft.aisocial.AuthPostgresIntegrationTest;
import ftn.userservice.domain.dtos.UserCreateRequest;
import ftn.userservice.domain.dtos.UserUpdateRequest;
import ftn.userservice.domain.entities.Role;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Set;

import static org.hamcrest.Matchers.*;
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
                .andExpect(content().contentType("application/json"));
                //.andExpect(jsonPath("$.id").value(userId));
        //TODO
    }

    @Test
    public void testUpdate() throws Exception {
        //TODO
        String userId = "e49fcaa5-4444-4444-4444-13e58187fea9";
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .email("standard@ais-test.com")
                .firstName("Updated")
                .lastName("User")
                .build();

        mockMvc.perform(put("/api/users/{userId}", userId)
                        .header("Authorization", "Bearer")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("standard@ais-test.com"))
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

}
