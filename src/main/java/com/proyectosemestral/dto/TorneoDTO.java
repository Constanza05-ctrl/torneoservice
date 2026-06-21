package com.proyectosemestral.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TorneoDTO {
    private Long id;
    private Long idTienda;
    private String NombreTienda;
    private String TipoTorneo;
    private Integer CantidadMiembros;
    private java.time.LocalDate FechaInicio;
    private java.time.LocalDate FechaTermino;
}