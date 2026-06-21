package com.proyectoSemestral.Torneo.Service;

import com.proyectosemestral.Model.Torneo;
import com.proyectosemestral.Service.TorneoService;
import com.proyectosemestral.TorneoApplication;
import com.proyectosemestral.dto.TorneoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TorneoApplication.class)
public class TorneoServiceTest {

    @Autowired
    private TorneoService torneoService;

    @Test
    public void testGuardarTorneoService() {
        Torneo torneo = new Torneo();
        torneo.setIdTienda(1L);
        torneo.setNombreTienda("TCG Store Santiago");
        torneo.setTipoTorneo("Draft");
        torneo.setCantidadMiembros(12);
        torneo.setFechaInicio(LocalDate.now());
        torneo.setFechaTermino(LocalDate.now().plusDays(2));

        TorneoDTO resultado = torneoService.guarTorneo(torneo);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Draft", resultado.getTipoTorneo());
    }
}