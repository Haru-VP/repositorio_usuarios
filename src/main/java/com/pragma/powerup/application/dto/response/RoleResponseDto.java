package com.pragma.powerup.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos del rol del usuario")
public class RoleResponseDto {

    @Schema(description = "Identificador numérico del rol", example = "2")
    private Long id;

    @Schema(description = "Nombre oficial del rol en el sistema", example = "PROPIETARIO")
    private String nombre;

    @Schema(description = "Descripción de las responsabilidades del rol", example = "Rol de propietario de restaurante")
    private String descripcion;
}
