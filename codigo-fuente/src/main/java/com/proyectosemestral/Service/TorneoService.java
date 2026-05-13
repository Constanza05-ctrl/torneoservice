package com.proyectosemestral.Service;

import com.proyectosemestral.Model.Torneo;
import com.proyectosemestral.Repository.TorneoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TorneoService {
    private final TorneoRepository torneoRepository;

    public TorneoService(TorneoRepository torneoRepository) {
        this.torneoRepository = torneoRepository;
    }

    public Torneo guarTorneo(Torneo torneo){
        return torneoRepository.save(torneo);
    }
    public List<Torneo> obtDatos() {
        return torneoRepository.findAll();
    }
    public Optional<Torneo> obtDatosId(Long id) {
        return torneoRepository.findById(id);
    }
    public Torneo actualizarTorneo(Long id, Torneo detalleTorneo) {
        Optional<Torneo> torneoExiste = torneoRepository.findById(id);

        if (torneoExiste.isPresent()) {
            Torneo torneoActualizar = torneoExiste.get();
            torneoActualizar.setNombreTienda(detalleTorneo.getNombreTienda());
            torneoActualizar.setTipoTorneo(detalleTorneo.getTipoTorneo());
            torneoActualizar.setCantidadMiembros(detalleTorneo.getCantidadMiembros());
            torneoActualizar.setFechaInicio(detalleTorneo.getFechaInicio());
            torneoActualizar.setFechaTermino(detalleTorneo.getFechaTermino());

            return torneoRepository.save(torneoActualizar);
        }else{
            return null;
        }
    }
    public boolean eliminarTorneo (long id) {
        if (torneoRepository.existsById(id)) {
            torneoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
