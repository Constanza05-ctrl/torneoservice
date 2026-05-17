package com.proyectosemestral.Service;

import com.proyectosemestral.Model.Torneo;
import com.proyectosemestral.Repository.TorneoRepository;
import com.proyectosemestral.Client.TiendaClient;
import com.proyectosemestral.dto.TiendaDTO;
import com.proyectosemestral.dto.TorneoDTO;
import com.proyectosemestral.exception.NotFoundException;
import com.proyectosemestral.exception.CaidoException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;
import feign.FeignException;

@Service
public class TorneoService {

    private final TorneoRepository torneoRepository;
    private final TiendaClient tiendaClient;
    private static final Logger log = LoggerFactory.getLogger(TorneoService.class);

    public TorneoService(TorneoRepository torneoRepository, TiendaClient tiendaClient) {
        this.torneoRepository = torneoRepository;
        this.tiendaClient = tiendaClient;
    }

    public Torneo guarTorneo(Torneo torneo) {
        log.info("Guardando torneo para la tienda ID: {}", torneo.getIdTienda());
        return torneoRepository.save(torneo);
    }

    public List<TorneoDTO> obtDatos() {
        log.info("Obteniendo todos los torneos con su información de tienda");
        List<Torneo> torneos = torneoRepository.findAll();

        return torneos.stream()
                .map(torneo -> ATorneoDTO(torneo))
                .collect(Collectors.toList());
    }


    public TorneoDTO obtDatosId(Long id) {
        log.info("Obteniendo torneo con ID: {}", id);

        Torneo torneo = torneoRepository.findById(id).orElseThrow(() -> {
            log.warn("Torneo con ID {} no encontrado", id);
            return new NotFoundException("Torneo no encontrado");
        });

        return ATorneoDTO(torneo);
    }

    public Torneo actualizarTorneo(Long id, Torneo detalleTorneo) {
        log.info("Actualizando torneo con ID: {}", id);

        Torneo torneoExiste = torneoRepository.findById(id).orElseThrow(() -> {
            log.warn("Torneo con ID {} no encontrado para actualización", id);
            return new NotFoundException("Torneo no encontrado");
        });

        torneoExiste.setIdTienda(detalleTorneo.getIdTienda());
        torneoExiste.setNombreTienda(detalleTorneo.getNombreTienda());
        torneoExiste.setTipoTorneo(detalleTorneo.getTipoTorneo());
        torneoExiste.setCantidadMiembros(detalleTorneo.getCantidadMiembros());
        torneoExiste.setFechaInicio(detalleTorneo.getFechaInicio());
        torneoExiste.setFechaTermino(detalleTorneo.getFechaTermino());

        log.info("Torneo actualizado localmente para tienda ID: {}", torneoExiste.getIdTienda());
        return torneoRepository.save(torneoExiste);
    }

    // 5. Eliminar Torneo
    public boolean eliminarTorneo(long id) {
        if (torneoRepository.existsById(id)) {
            torneoRepository.deleteById(id);
            log.info("Torneo eliminado con ID: {}", id);
            return true;
        }
        log.warn("Torneo con ID {} no encontrado", id);
        return false;
    }

    private TorneoDTO ATorneoDTO(Torneo torneo) {
        TiendaDTO tiendaInfo = null;
        try {
            log.info("Consumiendo módulo Tienda vía Feign para idTienda: {}", torneo.getIdTienda());
            tiendaInfo = tiendaClient.obtenerTiendaPorId(torneo.getIdTienda());

        } catch (FeignException e) {
            log.error("Error de Feign al comunicar con Tienda. Código Status: {}, Mensaje: {}",
                    e.status(), e.getMessage());
            throw new CaidoException("El microservicio de Tienda respondió con error (" + e.status() + ").");

        } catch (Exception e) {
            log.error("Fallo crítico de red o configuración: {}", e.getMessage());
            throw new CaidoException("No se pudo establecer comunicación con el microservicio externo de Tienda.");
        }

        TorneoDTO dto = new TorneoDTO();
        dto.setId(torneo.getId());
        dto.setIdTienda(torneo.getIdTienda());
        dto.setNombreTienda(tiendaInfo != null ? tiendaInfo.getNombre() : torneo.getNombreTienda());

        dto.setTipoTorneo(torneo.getTipoTorneo());
        dto.setCantidadMiembros(torneo.getCantidadMiembros());
        dto.setFechaInicio(torneo.getFechaInicio());
        dto.setFechaTermino(torneo.getFechaTermino());

        return dto;
    }
}