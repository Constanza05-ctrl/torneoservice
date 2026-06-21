package com.proyectosemestral.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.proyectosemestral.Model.Torneo;
import com.proyectosemestral.Service.TorneoService;
import com.proyectosemestral.dto.TorneoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/torneos")
public class TorneoController {

    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @Operation(summary = "Crear un nuevo torneo", description = "Procesa, valida vía Feign, guarda localmente y retorna el DTO del torneo creado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Torneo creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos")
    })
    @PostMapping
    public ResponseEntity<TorneoDTO> creaTorneo(@Valid @RequestBody Torneo torneo) {
        // El servicio procesa, valida vía Feign, guarda localmente y retorna el DTO
        TorneoDTO nuevoTorneoDTO = torneoService.guarTorneo(torneo);
        return new ResponseEntity<>(nuevoTorneoDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener lista completa de torneos", description = "Retorna una lista con todos los torneos registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de torneos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<TorneoDTO>> listaCompleta() {
        List<TorneoDTO> torneos = torneoService.obtDatos();
        return ResponseEntity.ok(torneos);
    }

    @Operation(summary = "Obtener datos de un torneo por ID", description = "Busca un torneo específico a través de su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Torneo encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Torneo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TorneoDTO> obtDatos(@PathVariable Long id) {
        // Tu servicio ya maneja el NotFoundException si no existe,
        // así que no hace falta validar si es null aquí.
        TorneoDTO torneoDTO = torneoService.obtDatosId(id);
        return ResponseEntity.ok(torneoDTO);
    }

    @Operation(summary = "Actualizar un torneo existente", description = "Modifica los datos de un torneo buscando por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Torneo actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Torneo no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización no válidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TorneoDTO> actualizarTorneo(@PathVariable Long id, @Valid @RequestBody Torneo torneo) {
        TorneoDTO torneoActualizadoDTO = torneoService.actualizarTorneo(id, torneo);
        return new ResponseEntity<>(torneoActualizadoDTO, HttpStatus.OK); // Retorna 200 OK con el DTO
    }

    @Operation(summary = "Eliminar un torneo", description = "Elimina de forma permanente un torneo por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Torneo eliminado exitosamente (No Content)"),
        @ApiResponse(responseCode = "404", description = "Torneo no encontrado")
    })
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