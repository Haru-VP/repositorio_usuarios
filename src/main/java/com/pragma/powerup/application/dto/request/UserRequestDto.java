package com.pragma.powerup.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos requeridos para la creación de un usuario con rol de propietario")
public class UserRequestDto {

    @Schema(description = "Nombre de pila del usuario", example = "Valentina")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pinto")
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Schema(description = "Documento de identidad (únicamente numérico)", example = "1098765432")
    @NotBlank(message = "El documento de identidad es obligatorio")
    private String documentoDeIdentidad;

    @Schema(description = "Número de celular (máximo 13 caracteres, admite prefijo +)", example = "+573001234567")
    @NotBlank(message = "El celular es obligatorio")
    private String celular;

    @Schema(description = "Fecha de nacimiento (el usuario debe ser mayor de 18 años)", example = "2000-01-15")
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fechaNacimiento;

    @Schema(description = "Correo electrónico institucional o personal válido", example = "valentina.propietario@plazoleta.com")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @Schema(description = "Contraseña en texto plano (será encriptada con BCrypt)", example = "ClaveSegura2026*")
    @NotBlank(message = "La clave es obligatoria")
    private String clave;
}
