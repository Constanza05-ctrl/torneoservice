package com.proyectosemestral.Client;
import com.proyectosemestral.dto.TiendaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Apunta directamente a la URL e instancia del microservicio de Tiendas
@FeignClient(name = "tienda-service", url = "${tienda.service.url:http://localhost:8081/tiendas}")
public interface TiendaClient {

    // Llama al GET por ID del controlador de Tienda
    @GetMapping("api/v2/tienda/{id}")
    TiendaDTO obtenerTiendaPorId(@PathVariable("id") Long id);
}
