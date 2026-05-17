package com.proyectosemestral.Controller;

import com.proyectosemestral.Model.Torneo;
import com.proyectosemestral.Service.TorneoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.*;



@RestController
@RequestMapping("/api/torneos")
public class TorneoController {

    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    @PostMapping
    public Torneo creaTorneo(@RequestBody Torneo torneo) {
        return torneoService.guarTorneo(torneo);
    }
    @GetMapping
    public List<Torneo> listaCompleta () {
        return torneoService.obtDatos();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Torneo> obtDatos(@PathVariable Long id) {
        Torneo torneo = torneoService.obtDatosId(id);

        if (torneo != null ) {
            return new ResponseEntity<>(torneo, HttpStatus.OK);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Torneo> actualizarTorneo (@PathVariable Long id, @RequestBody Torneo detalleTorneo) {
        Torneo torneoActual = torneoService.actualizarTorneo(id, detalleTorneo);
        if (torneoActual != null) {
            return ResponseEntity.ok(torneoActual);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTorneo (@PathVariable Long id) {
        boolean eliminadoTorneo = torneoService.eliminarTorneo(id);
        if (eliminadoTorneo) {
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
   
 }
    
