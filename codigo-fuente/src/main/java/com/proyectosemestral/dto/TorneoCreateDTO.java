package com.proyectosemestral.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Datos para crear un torneo")
public class TorneoCreateDTO {

    @Schema(description = "id de tienda", example = "3")
    @NotNull(message = "El ID de la tienda no puede ser nulo")
    private Long idTienda;

    @Schema(description = "nombre de tienda", example = "NeXtCard")
    @NotBlank(message = "El nombre de la tienda es obligatorio")
    private String nombreTienda;   

    @Schema(description = "tipo de torneo", example = "Yu-gi-oh Locals")
    @NotBlank(message = "El tipo de torneo es obligatorio")
    private String tipoTorneo;

    @Schema(description = "cantidad de participantes (no menor a 2)", example = "30")
    @NotNull(message = "La cantidad de miembros no puede ser nula")
    @Min(value = 2, message = "El torneo debe tener al menos 2 miembros")
    private Integer cantidadMiembros; 
    
    @Schema(description = "fecha de inicio", example = "2020-10-24")
    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio debe ser hoy o en el futuro")
    private LocalDate fechaInicio;

    @Schema(description = "fecha de termino", example = "2020-10-25")
    @NotNull(message = "La fecha de término es obligatoria")
    private LocalDate fechaTermino;
}
