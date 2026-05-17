package com.proyectosemestral.Service;

import com.proyectosemestral.Model.Torneo;
import com.proyectosemestral.Repository.TorneoRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import com.proyectosemestral.exception.NotFoundException;
@Service
public class TorneoService {
    private final TorneoRepository torneoRepository;
    private static final Logger log = LoggerFactory.getLogger(TorneoService.class);

    public TorneoService(TorneoRepository torneoRepository) {
        this.torneoRepository = torneoRepository;
    }

    public Torneo guarTorneo(Torneo torneo){
        log.info("Guardando torneo: {}", torneo.getNombreTienda());
        return torneoRepository.save(torneo);
    }
    public List<Torneo> obtDatos() {
        log.info("Obteniendo todos los torneos");
        return torneoRepository.findAll();
    }
    public Torneo obtDatosId(Long id) {
        log.info("Obteniendo torneo con ID: {}", id);

        return torneoRepository.findById(id).orElseThrow(() -> {
            log.warn("Torneo con ID {} no encontrado", id);
            return new NotFoundException("Torneo no encontrado");
        });
    }
    public Torneo actualizarTorneo(Long id, Torneo detalleTorneo) {
        log.info("Actualizando torneo con ID: {}", id);
        Torneo torneoExiste = torneoRepository.findById(id).orElseThrow(() -> {
            log.warn("Torneo con ID {} no encontrado para actualización", id);
            return new NotFoundException("Torneo no encontrado");
        });

        if (torneoExiste != null) {
            Torneo torneoActualizar = torneoExiste;
            torneoActualizar.setNombreTienda(detalleTorneo.getNombreTienda());
            torneoActualizar.setTipoTorneo(detalleTorneo.getTipoTorneo());
            torneoActualizar.setCantidadMiembros(detalleTorneo.getCantidadMiembros());
            torneoActualizar.setFechaInicio(detalleTorneo.getFechaInicio());
            torneoActualizar.setFechaTermino(detalleTorneo.getFechaTermino());
            log.info("Torneo actualizado: {}", torneoActualizar.getNombreTienda());
            return torneoRepository.save(torneoActualizar);
        }else{
            log.warn("Torneo con ID {} no encontrado", id);
            return null;
        }
    }
    public boolean eliminarTorneo (long id) {
        if (torneoRepository.existsById(id)) {
            torneoRepository.deleteById(id);
            log.info("Torneo eliminado con ID: {}", id);
            return true;
        }
        log.warn("Torneo con ID {} no encontrado", id);
        return false;
    }
}
