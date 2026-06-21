package com.proyectosemestral.Client;

import com.proyectosemestral.dto.TiendaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Removemos el "/tiendas" del fallback para dejar la URL base limpia
@FeignClient(name = "tienda-service", url = "${TIENDA_URL:http://localhost:8081}")
public interface TiendaClient {

    // Ruta exacta que coincide con el controlador de tu compañera ({F3F3BDCD})
    @GetMapping("/api/v2/tienda/{id}")
    TiendaDTO obtenerTiendaPorId(@PathVariable("id") Long id);
}