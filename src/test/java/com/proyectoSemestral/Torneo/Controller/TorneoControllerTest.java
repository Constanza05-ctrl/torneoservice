package com.proyectosemestral.Torneo.Controller;

import com.proyectosemestral.Controller.TorneoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TorneoControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TorneoController torneoController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(torneoController).build();
    }

    @Test
    public void testStatusEndpoints() throws Exception {
        mockMvc.perform(get("/api/torneos")) 
               .andExpect(status().isOk());
    }
}