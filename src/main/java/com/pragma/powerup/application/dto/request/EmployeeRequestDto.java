package com.pragma.powerup.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos requeridos para la creación de un usuario con rol de empleado")
public class EmployeeRequestDto {

    @Schema(description = "Nombre de pila del empleado", example = "Carlos")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(description = "Apellido del empleado", example = "Gómez")
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Schema(description = "Documento de identidad (únicamente numérico)", example = "1098765432")
    @NotBlank(message = "El documento de identidad es obligatorio")
    private String documentoDeIdentidad;

    @Schema(description = "Número de celular (máximo 13 caracteres, admite prefijo +)", example = "+573001234567")
    @NotBlank(message = "El celular es obligatorio")
    private String celular;

    @Schema(description = "Fecha de nacimiento (opcional)", example = "1998-05-20", required = false)
    private LocalDate fechaNacimiento;

    @Schema(description = "Correo electrónico válido", example = "carlos.empleado@restaurante.com")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @Schema(description = "Contraseña en texto plano (será encriptada con BCrypt)", example = "ClaveSegura2026*")
    @NotBlank(message = "La clave es obligatoria")
    private String clave;

    @Schema(description = "Identificador del rol (3 para EMPLEADO)", example = "3", required = false)
    private Long idRol;
}
