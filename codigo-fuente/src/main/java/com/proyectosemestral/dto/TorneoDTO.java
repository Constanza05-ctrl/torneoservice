package com.proyectosemestral.dto;

import lombok.Data;

@Data
public class TorneoDTO {
    private Long id;
    private String NombreTienda;
    private String TipoTorneo;
    private Integer CantidadMiembros;
    private java.time.LocalDate FechaInicio;
    private java.time.LocalDate FechaTermino;
}