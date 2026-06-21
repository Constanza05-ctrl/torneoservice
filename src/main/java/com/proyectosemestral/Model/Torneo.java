package com.proyectosemestral.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID de la tienda no puede ser nulo")
    private Long idTienda;

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
