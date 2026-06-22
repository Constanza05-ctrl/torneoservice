package com.proyectoSemestral.Torneo.Controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.proyectosemestral.Controller.TorneoController;
import com.proyectosemestral.Service.TorneoService;
import com.proyectosemestral.dto.TorneoDTO;
import com.proyectosemestral.exception.*;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TorneoControllerTest {

    @Mock
    private TorneoService service;

    @InjectMocks
    private TorneoController tiendaController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(tiendaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /api/torneos - debe retornar 200 con la lista de torneos")
    void debeRetornar200CuandoSePiden() throws Exception {
        // Given
        when(service.obtDatos()).thenReturn(List.of(
                new TorneoDTO(1L, 10L, "Magic Store Chile", "Standard TCG", 16, LocalDate.of(2026, 7, 1),
                        LocalDate.of(2026, 7, 2))));

        // When & Then
        mockMvc.perform(get("/api/torneos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombreTienda").value("Magic Store Chile"))
                .andExpect(jsonPath("$[0].tipoTorneo").value("Standard TCG"));
    }

    @Test
    @DisplayName("GET /api/torneos/{id} - debe retornar 404 cuando el torneo no existe")
    void debeRetornar404CuandoTorneoNoExiste() throws Exception {
        // Given
        when(service.obtDatosId(999L))
                .thenThrow(new NotFoundException("Torneo no encontrado"));

        // When & Then
        mockMvc.perform(get("/api/torneos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/torneos - debe retornar 201 al crear un torneo válido")
    void debeRetornar201AlCrearTorneo() throws Exception {
        // Given
        String json = """
                {
                    "idTienda": 10,
                    "nombreTienda": "Magic Store Chile",
                    "tipoTorneo": "Modern TCG",
                    "cantidadMiembros": 8,
                    "fechaInicio": "2026-07-20",
                    "fechaTermino": "2026-07-22"
                }
                """;

        when(service.guarTorneo(any())).thenReturn(
                new TorneoDTO(1L, 10L, "Magic Store Chile", "Modern TCG", 8, LocalDate.of(2026, 7, 20),
                        LocalDate.of(2026, 7, 22)));

        // When & Then
        mockMvc.perform(post("/api/torneos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombreTienda").value("Magic Store Chile"))
                .andExpect(jsonPath("$.tipoTorneo").value("Modern TCG"));
    }

    @Test
    @DisplayName("POST /api/torneos - debe retornar 400 cuando los datos son inválidos")
    void debeRetornar400CuandoNombreEstaVacio() throws Exception {
        String json = """
                {
                    "idTienda": null,
                    "nombreTienda": "",
                    "tipoTorneo": "Yu-Gi-Oh! TCG",
                    "cantidadMiembros": 1,
                    "fechaInicio": "2020-01-01",
                    "fechaTermino": "2020-01-02"
                }
                """;
        mockMvc.perform(post("/api/torneos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }
}