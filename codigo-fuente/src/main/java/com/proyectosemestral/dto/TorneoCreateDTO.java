package com.proyectosemestral.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TorneoCreateDTO {
    @NotBlank(message = "El nombre de la tienda es obligatorio")
    private String nombreTienda;   

    @NotBlank(message = "El tipo de torneo es obligatorio")
    private String tipoTorneo;      

    @NotNull(message = "La cantidad de miembros no puede ser nula")
    @Min(value = 2, message = "El torneo debe tener al menos 2 miembros")
    private Integer cantidadMiembros; 
    
    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio debe ser hoy o en el futuro")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de término es obligatoria")
    private LocalDate fechaTermino;
}
