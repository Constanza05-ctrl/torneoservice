package com.proyectosemestral.Controller;

import com.proyectosemestral.Model.Torneo;
import com.proyectosemestral.Service.TorneoService;
import com.proyectosemestral.dto.TorneoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/torneos")
public class TorneoController {

    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @PostMapping
    public ResponseEntity<TorneoDTO> creaTorneo(@RequestBody Torneo torneo) {
        // El servicio procesa, valida vía Feign, guarda localmente y retorna el DTO
        TorneoDTO nuevoTorneoDTO = torneoService.guarTorneo(torneo);
        return new ResponseEntity<>(nuevoTorneoDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TorneoDTO>> listaCompleta() {
        List<TorneoDTO> torneos = torneoService.obtDatos();
        return ResponseEntity.ok(torneos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TorneoDTO> obtDatos(@PathVariable Long id) {
        // Tu servicio ya maneja el NotFoundException si no existe,
        // así que no hace falta validar si es null aquí.
        TorneoDTO torneoDTO = torneoService.obtDatosId(id);
        return ResponseEntity.ok(torneoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TorneoDTO> actualizarTorneo(@PathVariable Long id, @RequestBody Torneo torneo) {
        TorneoDTO torneoActualizadoDTO = torneoService.actualizarTorneo(id, torneo);
        return new ResponseEntity<>(torneoActualizadoDTO, HttpStatus.OK); // Retorna 200 OK con el DTO
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTorneo(@PathVariable Long id) {
        boolean eliminadoTorneo = torneoService.eliminarTorneo(id);
        if (eliminadoTorneo) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}