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

    public TorneoDTO guarTorneo(Torneo torneo) {
        com.proyectosemestral.dto.TiendaDTO tiendaRemota = null;

        try {
            log.info("Validando existencia de la tienda ID: {} antes de crear el torneo", torneo.getIdTienda());

            // 1. Invocamos el cliente Feign hacia el microservicio remoto
            tiendaRemota = tiendaClient.obtenerTiendaPorId(torneo.getIdTienda());

            // 2. Extraemos el nombre de la tienda y se lo inyectamos al torneo
            // Esto evita la excepción 'El nombre de la tienda es obligatorio' de Hibernate
            if (tiendaRemota != null && tiendaRemota.getNombre() != null) {
                torneo.setNombreTienda(tiendaRemota.getNombre());
                log.info("Tienda validada correctamente. Nombre asignado: {}", torneo.getNombreTienda());
            }

        } catch (FeignException e) {
            if (e.status() == 404) {
                log.error("Error: La tienda con ID {} no existe en el sistema remoto.", torneo.getIdTienda());
                throw new NotFoundException(
                        "No se puede crear el torneo porque la tienda con ID " + torneo.getIdTienda() + " no existe.");
            }
            if (e.status() < 0) {
                log.error("Error de red con el microservicio remoto de Tienda: {}", e.getMessage());
                throw new CaidoException("No se pudo validar la tienda porque el servidor remoto está inaccesible.");
            }
            throw new CaidoException(
                    "El servicio de Tiendas devolvió un error inesperado (Código HTTP: " + e.status() + ").");
        } catch (Exception e) {
            log.error("Fallo general de comunicación: {}", e.getMessage());
            throw new CaidoException("El microservicio de Tienda se encuentra inaccesible. Inténtelo más tarde.");
        }

        // 3. Almacenamos el torneo con su 'nombreTienda' ya inyectado en tu base de
        // datos
        Torneo torneoGuardado = torneoRepository.save(torneo);

        // 4. Mapeamos la entidad persistida a un TorneoDTO para la salida limpia
        TorneoDTO dto = new TorneoDTO();
        dto.setId(torneoGuardado.getId());
        dto.setIdTienda(torneoGuardado.getIdTienda());
        dto.setNombreTienda(torneoGuardado.getNombreTienda());
        dto.setTipoTorneo(torneoGuardado.getTipoTorneo());
        dto.setCantidadMiembros(torneoGuardado.getCantidadMiembros());
        dto.setFechaInicio(torneoGuardado.getFechaInicio());
        dto.setFechaTermino(torneoGuardado.getFechaTermino());

        return dto;
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

    public TorneoDTO actualizarTorneo(Long id, Torneo torneoDetalles) {
        // 1. Verificar si el torneo que se quiere editar existe en tu BD local
        Torneo torneoExistente = torneoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "No se puede actualizar porque el torneo con ID " + id + " no existe."));

        com.proyectosemestral.dto.TiendaDTO tiendaRemota = null;

        try {
            log.info("Validando existencia de la tienda ID: {} para la actualización del torneo",
                    torneoDetalles.getIdTienda());
            tiendaRemota = tiendaClient.obtenerTiendaPorId(torneoDetalles.getIdTienda());

            // 3. Si la tienda existe, inyectamos el nombre recuperado por red
            if (tiendaRemota != null && tiendaRemota.getNombre() != null) {
                torneoExistente.setNombreTienda(tiendaRemota.getNombre());
            }

        } catch (FeignException e) {
            if (e.status() == 404) {
                log.error("Validación fallida: La tienda ID {} no existe.", torneoDetalles.getIdTienda());
                throw new NotFoundException("No se puede actualizar el torneo porque la tienda con ID "
                        + torneoDetalles.getIdTienda() + " no existe.");
            }
            if (e.status() < 0) {
                log.error("Fallo de red con el microservicio de Tienda: {}", e.getMessage());
                throw new CaidoException("No se pudo validar la tienda porque el servidor remoto está inaccesible.");
            }
            throw new CaidoException("El servicio remoto de Tienda respondió con error (Status: " + e.status() + ").");
        } catch (Exception e) {
            log.error("Fallo crítico colateral: {}", e.getMessage());
            throw new CaidoException("El microservicio de Tienda se encuentra inaccesible.");
        }

        torneoExistente.setIdTienda(torneoDetalles.getIdTienda());
        torneoExistente.setTipoTorneo(torneoDetalles.getTipoTorneo());
        torneoExistente.setCantidadMiembros(torneoDetalles.getCantidadMiembros());
        torneoExistente.setFechaInicio(torneoDetalles.getFechaInicio());
        torneoExistente.setFechaTermino(torneoDetalles.getFechaTermino());

        Torneo torneoActualizado = torneoRepository.save(torneoExistente);
        TorneoDTO dto = new TorneoDTO();
        dto.setId(torneoActualizado.getId());
        dto.setIdTienda(torneoActualizado.getIdTienda());
        dto.setNombreTienda(torneoActualizado.getNombreTienda());
        dto.setTipoTorneo(torneoActualizado.getTipoTorneo());
        dto.setCantidadMiembros(torneoActualizado.getCantidadMiembros());
        dto.setFechaInicio(torneoActualizado.getFechaInicio());
        dto.setFechaTermino(torneoActualizado.getFechaTermino());
        log.info("Torneo con ID {} actualizado exitosamente", id);
        return dto;
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