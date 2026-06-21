package com.proyectoSemestral.Torneo.Repository;

import com.proyectosemestral.Model.Torneo;
import com.proyectosemestral.Repository.TorneoRepository;
import com.proyectosemestral.TorneoApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = TorneoApplication.class) 
class TorneoRepositoryTest {

    @Autowired
    private TorneoRepository repository;

    @Test
    @DisplayName("save - debe persistir el torneo y asignar un ID generado automáticamente")
    void debePersistirTorneoYAsignarIdGenerado() {
        // Given
        Torneo torneo = new Torneo(null, 10L, "Magic Store Chile", "Standard TCG", 16, 
            LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 2));

        // When
        Torneo guardado = repository.save(torneo);

        // Then
        assertNotNull(guardado.getId());
        assertTrue(guardado.getId() > 0);
        assertEquals("Magic Store Chile", guardado.getNombreTienda());
        assertEquals("Standard TCG", guardado.getTipoTorneo());
        assertEquals(16, guardado.getCantidadMiembros());
    }

    @Test
    @DisplayName("findAll - debe retornar todos los torneos guardados en la BD")
    void debeRetornarTodosLosTorneosGuardados() {
        // Given
        repository.save(new Torneo(null, 10L, "Magic Store Chile", "Commander", 32, 
            LocalDate.of(2026, 8, 10), LocalDate.of(2026, 8, 11)));
        repository.save(new Torneo(null, 11L, "NextCard Store", "Yu-Gi-Oh! Locals", 8, 
            LocalDate.of(2026, 9, 20), LocalDate.of(2026, 9, 21)));

        // When
        List<Torneo> torneos = repository.findAll();

        // Then
        assertNotNull(torneos);
        assertEquals(2, torneos.size());
    }

    @Test
    @DisplayName("findById - debe retornar el torneo correcto cuando el ID existe")
    void debeEncontrarTorneoPorIdExistente() {
        // Given
        Torneo guardado = repository.save(
            new Torneo(null, 10L, "Magic Store Chile", "Modern TCG", 16, 
                LocalDate.of(2026, 7, 20), LocalDate.of(2026, 7, 22))
        );

        // When
        Optional<Torneo> resultado = repository.findById(guardado.getId());

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("Magic Store Chile", resultado.get().getNombreTienda());
        assertEquals("Modern TCG", resultado.get().getTipoTorneo());
    }

    @Test
    @DisplayName("findById - debe retornar Optional vacío cuando el ID no existe")
    void debeRetornarOptionalVacioCuandoIdNoExiste() {
        // When
        Optional<Torneo> resultado = repository.findById(999L);

        // Then
        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("deleteById - debe eliminar el torneo de la base de datos")
    void debeEliminarTorneoPorId() {
        // Given
        Torneo guardado = repository.save(
            new Torneo(null, 12L, "Kurogami TCG", "One Piece Card Game", 24, 
                LocalDate.of(2026, 10, 5), LocalDate.of(2026, 10, 6))
        );
        Long id = guardado.getId();

        // When
        repository.deleteById(id);

        // Then
        assertFalse(repository.findById(id).isPresent());
    }
}