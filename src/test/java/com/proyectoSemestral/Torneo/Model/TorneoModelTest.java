package com.proyectosemestral.Torneo.Model;

import com.proyectosemestral.Model.Torneo;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class TorneoModelTest {

    @Test
    public void testCrearTorneo() {
        Torneo torneo = new Torneo();
        LocalDate hoy = LocalDate.now();
        
        torneo.setId(1L);
        torneo.setIdTienda(10L);
        torneo.setNombreTienda("Tienda Central");
        torneo.setTipoTorneo("Draft Pokémon");
        torneo.setCantidadMiembros(16);
        torneo.setFechaInicio(hoy);
        torneo.setFechaTermino(hoy.plusDays(5));

        assertNotNull(torneo);
        assertEquals(1L, torneo.getId());
        assertEquals(10L, torneo.getIdTienda());
        assertEquals("Tienda Central", torneo.getNombreTienda());
        assertEquals("Draft Pokémon", torneo.getTipoTorneo());
        assertEquals(16, torneo.getCantidadMiembros());
    }
}