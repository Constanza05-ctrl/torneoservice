package com.proyectoSemestral.Torneo.Repository;

import com.proyectosemestral.Model.Torneo;
import com.proyectosemestral.Repository.TorneoRepository;
import com.proyectosemestral.TorneoApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TorneoApplication.class)
public class TorneoRepositoryTest {

    @Autowired
    private TorneoRepository torneoRepository;

    @Test
    public void testOperacionesBaseDatos() {
        Torneo torneo = new Torneo();
        torneo.setIdTienda(20L);
        torneo.setNombreTienda("Tienda Mall");
        torneo.setTipoTorneo("Liga Semestral");
        torneo.setCantidadMiembros(8);
        torneo.setFechaInicio(LocalDate.now());
        torneo.setFechaTermino(LocalDate.now().plusMonths(1));

        Torneo guardado = torneoRepository.save(torneo);
        Optional<Torneo> encontrado = torneoRepository.findById(guardado.getId());
        List<Torneo> todos = torneoRepository.findAll();

        assertTrue(encontrado.isPresent());
        assertEquals("Tienda Mall", encontrado.get().getNombreTienda());
        assertEquals("Liga Semestral", encontrado.get().getTipoTorneo());
        assertFalse(todos.isEmpty());
    }
}