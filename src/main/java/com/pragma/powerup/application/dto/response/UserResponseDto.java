package com.pragma.powerup.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos completos del usuario consultado")
public class UserResponseDto {

    @Schema(description = "Identificador único del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre de pila del usuario", example = "Valentina")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pinto")
    private String apellido;

    @Schema(description = "Documento de identidad", example = "1098765432")
    private String documentoDeIdentidad;

    @Schema(description = "Número de teléfono celular", example = "+573001234567")
    private String celular;

    @Schema(description = "Fecha de nacimiento", example = "2000-01-15")
    private LocalDate fechaNacimiento;

    @Schema(description = "Correo electrónico registrado", example = "valentina.propietario@plazoleta.com")
    private String correo;

    @Schema(description = "Información del rol asignado al usuario")
    private RoleResponseDto rol;
}
