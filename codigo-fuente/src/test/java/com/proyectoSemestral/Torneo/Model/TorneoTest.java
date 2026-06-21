package com.proyectoSemestral.Torneo.Model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.proyectosemestral.Model.Torneo;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TorneoTest {

    @Test
    @DisplayName("Constructor vacío - debe crear una instancia no nula")
    void constructorVacioDebeCrearInstanciaNoNula() {
        Torneo torneo = new Torneo();
        assertNotNull(torneo);
    }

    @Test
    @DisplayName("Constructor completo - debe asignar todos los campos correctamente")
    void constructorCompletoDebeAsignarTodosLosCampos() {
        // Given & When
        Torneo torneo = new Torneo(
            1L, 10L, "Magic Store Chile", "Standard TCG", 16, 
            LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 2)
        );

        // Then
        assertEquals(1L, torneo.getId());
        assertEquals(10L, torneo.getIdTienda());
        assertEquals("Magic Store Chile", torneo.getNombreTienda());
        assertEquals("Standard TCG", torneo.getTipoTorneo());
        assertEquals(16, torneo.getCantidadMiembros());
        assertEquals(LocalDate.of(2026, 7, 1), torneo.getFechaInicio());
        assertEquals(LocalDate.of(2026, 7, 2), torneo.getFechaTermino());
    }

    @Test
    @DisplayName("Setters - debe permitir modificar cada campo individualmente")
    void settersDebenPermitirModificarCampos() {
        // Given
        Torneo torneo = new Torneo();

        // When
        torneo.setId(2L);
        torneo.setIdTienda(20L);
        torneo.setNombreTienda("NextCard Store");
        torneo.setTipoTorneo("Commander");
        torneo.setCantidadMiembros(32);
        torneo.setFechaInicio(LocalDate.of(2026, 8, 15));
        torneo.setFechaTermino(LocalDate.of(2026, 8, 16));

        // Then
        assertEquals(2L, torneo.getId());
        assertEquals(20L, torneo.getIdTienda());
        assertEquals("NextCard Store", torneo.getNombreTienda());
        assertEquals("Commander", torneo.getTipoTorneo());
        assertEquals(32, torneo.getCantidadMiembros());
        assertEquals(LocalDate.of(2026, 8, 15), torneo.getFechaInicio());
        assertEquals(LocalDate.of(2026, 8, 16), torneo.getFechaTermino());
    }

    @Test
    @DisplayName("equals y hashCode - dos torneos con los mismos datos deben ser iguales")
    void dosTorneosConMismosDatosDebenSerIguales() {
        // Given
        Torneo t1 = new Torneo(1L, 10L, "Magic Store Chile", "Standard TCG", 16, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 2));
        Torneo t2 = new Torneo(1L, 10L, "Magic Store Chile", "Standard TCG", 16, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 2));

        // When & Then
        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    @DisplayName("toString - debe contener el tipo de torneo en su representación")
    void toStringDebeContenerTipoDeTorneo() {
        // Given
        Torneo torneo = new Torneo(3L, 15L, "Yu-Gi-Oh Store", "Yu-Gi-Oh! Locals", 8, LocalDate.of(2026, 9, 10), LocalDate.of(2026, 9, 11));

        // When
        String texto = torneo.toString();

        // Then
        assertNotNull(texto);
        assertTrue(texto.contains("Yu-Gi-Oh! Locals"));
    }
}
