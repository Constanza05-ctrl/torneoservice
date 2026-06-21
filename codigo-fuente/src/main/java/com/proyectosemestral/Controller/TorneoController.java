package com.proyectosemestral.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.proyectosemestral.Model.Torneo;
import com.proyectosemestral.Service.TorneoService;
import com.proyectosemestral.dto.TorneoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import jakarta.validation.Valid;

@Tag(name = "Modulo de Torneos", description = "Modulo de CRUD de torneos")
@RestController
@RequestMapping("/api/torneos")
public class TorneoController {

    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @Operation(summary = "Crear un nuevo torneo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping
    public ResponseEntity<TorneoDTO> creaTorneo(@Valid @io.swagger.v3.oas.annotations
  .parameters.RequestBody(description = "datos para la creación de torneo") @RequestBody Torneo torneo) {
        TorneoDTO nuevoTorneoDTO = torneoService.guarTorneo(torneo);
        return new ResponseEntity<>(nuevoTorneoDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener lista completa de torneos")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping
    public ResponseEntity<List<TorneoDTO>> listaCompleta() {
        List<TorneoDTO> torneos = torneoService.obtDatos();
        return ResponseEntity.ok(torneos);
    }

    @Operation(summary = "Obtener torneo por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TorneoDTO> obtDatos(
            @Parameter(description = "ID del torneo", required = true) @PathVariable Long id) {
        TorneoDTO torneoDTO = torneoService.obtDatosId(id);
        return ResponseEntity.ok(torneoDTO);
    }

    @Operation(summary = "Actualizar un torneo existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TorneoDTO> actualizarTorneo(
            @Parameter(description = "ID del torneo", required = true) @PathVariable Long id, 
            @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos para crear torneo") @RequestBody Torneo torneo) {
        TorneoDTO torneoActualizadoDTO = torneoService.actualizarTorneo(id, torneo);
        return new ResponseEntity<>(torneoActualizadoDTO, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar un torneo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No Content"),
        @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTorneo(
            @Parameter(description = "ID del torneo", required = true) @PathVariable Long id) {
        boolean eliminadoTorneo = torneoService.eliminarTorneo(id);
        if (eliminadoTorneo) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}