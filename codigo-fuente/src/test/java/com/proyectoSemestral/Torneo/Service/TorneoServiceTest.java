package com.proyectoSemestral.Torneo.Service;

import com.proyectosemestral.Client.TiendaClient;
import com.proyectosemestral.Model.Torneo;
import com.proyectosemestral.Repository.TorneoRepository;
import com.proyectosemestral.Service.TorneoService;
import com.proyectosemestral.dto.TiendaDTO;
import com.proyectosemestral.dto.TorneoDTO;
import com.proyectosemestral.exception.CaidoException;
import com.proyectosemestral.exception.NotFoundException;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TorneoServiceTest {

    @Mock
    private TorneoRepository torneoRepository;

    @Mock
    private TiendaClient tiendaClient;

    @InjectMocks
    private TorneoService torneoService;

    // ── guarTorneo (Crear) ─────────────────────────────────────────────────────

    @Test
    @DisplayName("guarTorneo - debe persistir el torneo cuando la tienda existe remotamente")
    void debeGuardarTorneoCorrectamente() {
        // Given
        Torneo torneoInput = new Torneo(null, 10L, null, "Standard TCG", 16, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 2));
        TiendaDTO tiendaSimulada = new TiendaDTO(10L, "Magic Store Chile");
        Torneo torneoGuardado = new Torneo(1L, 10L, "Magic Store Chile", "Standard TCG", 16, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 2));

        when(tiendaClient.obtenerTiendaPorId(10L)).thenReturn(tiendaSimulada);
        when(torneoRepository.save(any(Torneo.class))).thenReturn(torneoGuardado);

        // When
        TorneoDTO resultado = torneoService.guarTorneo(torneoInput);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Magic Store Chile", resultado.getNombreTienda());
        verify(tiendaClient, times(1)).obtenerTiendaPorId(10L);
        verify(torneoRepository, times(1)).save(any(Torneo.class));
    }

    @Test
    @DisplayName("guarTorneo - debe lanzar NotFoundException si Feign devuelve un 404")
    void debeLanzarNotFoundAlGuardarTorneoConTiendaInexistente() {
        // Given
        Torneo torneoInput = new Torneo(null, 999L, null, "Modern", 8, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 2));
        
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(404);
        when(tiendaClient.obtenerTiendaPorId(999L)).thenThrow(feignException);

        // When & Then
        assertThrows(NotFoundException.class, () -> torneoService.guarTorneo(torneoInput));
        verify(torneoRepository, never()).save(any());
    }

    @Test
    @DisplayName("guarTorneo - debe lanzar CaidoException si falla la red (status < 0)")
    void debeLanzarCaidoExceptionCuandoFallaRedRemota() {
        // Given
        Torneo torneoInput = new Torneo(null, 10L, null, "Commander", 4, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 2));
        
        FeignException feignException = mock(FeignException.class);
        when(feignException.status()).thenReturn(-1);
        when(tiendaClient.obtenerTiendaPorId(10L)).thenThrow(feignException);

        // When & Then
        assertThrows(CaidoException.class, () -> torneoService.guarTorneo(torneoInput));
    }

    // ── obtDatos (Listar Todos) ────────────────────────────────────────────────

    @Test
    @DisplayName("obtDatos - debe retornar lista de DTOs mapeados con datos de Feign")
    void debeRetornarListaDeTorneos() {
        // Given
        List<Torneo> torneosEnBD = List.of(
            new Torneo(1L, 10L, "Magic Store", "Standard", 16, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 2))
        );
        TiendaDTO tiendaSimulada = new TiendaDTO(10L, "Magic Store Chile");

        when(torneoRepository.findAll()).thenReturn(torneosEnBD);
        when(tiendaClient.obtenerTiendaPorId(10L)).thenReturn(tiendaSimulada);

        // When
        List<TorneoDTO> resultado = torneoService.obtDatos();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Magic Store Chile", resultado.get(0).getNombreTienda());
    }

    // ── obtDatosId (Buscar por ID) ─────────────────────────────────────────────

    @Test
    @DisplayName("obtDatosId - debe retornar el torneo correcto por ID")
    void debeRetornarTorneoPorIdExistente() {
        // Given
        Torneo torneo = new Torneo(1L, 10L, "Gamer Paradise", "Draft", 8, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 2));
        TiendaDTO tiendaSimulada = new TiendaDTO(10L, "Gamer Paradise Real");

        when(torneoRepository.findById(1L)).thenReturn(Optional.of(torneo));
        when(tiendaClient.obtenerTiendaPorId(10L)).thenReturn(tiendaSimulada);

        // When
        TorneoDTO resultado = torneoService.obtDatosId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals("Gamer Paradise Real", resultado.getNombreTienda());
    }

    @Test
    @DisplayName("obtDatosId - debe lanzar NotFoundException si el torneo no existe localmente")
    void debeLanzarNotFoundSiTorneoNoExisteLocal() {
        // Given
        when(torneoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> torneoService.obtDatosId(999L));
    }

    // ── actualizarTorneo ───────────────────────────────────────────────────────

    @Test
    @DisplayName("actualizarTorneo - debe actualizar campos y persistir cambios")
    void debeActualizarTorneoCorrectamente() {
        // Given
        Torneo existente = new Torneo(1L, 10L, "Original Store", "Standard", 16, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 2));
        Torneo modificados = new Torneo(null, 11L, null, "Modern", 32, LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 2));
        TiendaDTO tiendaSimulada = new TiendaDTO(11L, "New Store Chile");
        Torneo guardado = new Torneo(1L, 11L, "New Store Chile", "Modern", 32, LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 2));

        when(torneoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tiendaClient.obtenerTiendaPorId(11L)).thenReturn(tiendaSimulada);
        when(torneoRepository.save(any(Torneo.class))).thenReturn(guardado);

        // When
        TorneoDTO resultado = torneoService.actualizarTorneo(1L, modificados);

        // Then
        assertNotNull(resultado);
        assertEquals("Modern", resultado.getTipoTorneo());
        assertEquals(32, resultado.getCantidadMiembros());
        assertEquals("New Store Chile", resultado.getNombreTienda());
    }

    // ── eliminarTorneo ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("eliminarTorneo - debe retornar true y ejecutar delete si el ID existe")
    void debeEliminarTorneoCuandoExiste() {
        // Given
        when(torneoRepository.existsById(1L)).thenReturn(true);

        // When
        boolean resultado = torneoService.eliminarTorneo(1L);

        // Then
        assertTrue(resultado);
        verify(torneoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminarTorneo - debe retornar false si el ID no existe")
    void debeRetornarFalseAlEliminarTorneoInexistente() {
        // Given
        when(torneoRepository.existsById(999L)).thenReturn(false);

        // When
        boolean resultado = torneoService.eliminarTorneo(999L);

        // Then
        assertFalse(resultado);
        verify(torneoRepository, never()).deleteById(anyLong());
    }
}