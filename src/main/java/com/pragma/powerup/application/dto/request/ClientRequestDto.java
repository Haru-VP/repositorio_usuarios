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
@Schema(description = "Datos requeridos para la creación de una cuenta de cliente")
public class ClientRequestDto {

    @Schema(description = "Nombre de pila del cliente", example = "Laura")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(description = "Apellido del cliente", example = "Jimenez")
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Schema(description = "Documento de identidad (únicamente numérico)", example = "1098765432")
    @NotBlank(message = "El documento de identidad es obligatorio")
    private String documentoDeIdentidad;

    @Schema(description = "Número de celular (máximo 13 caracteres, admite prefijo +)", example = "+573001234567")
    @NotBlank(message = "El celular es obligatorio")
    private String celular;

    @Schema(description = "Fecha de nacimiento (opcional)", example = "1999-10-12", required = false)
    private LocalDate fechaNacimiento;

    @Schema(description = "Correo electrónico válido", example = "laura.cliente@correo.com")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @Schema(description = "Contraseña en texto plano (será encriptada con BCrypt)", example = "ClaveCliente2026*")
    @NotBlank(message = "La clave es obligatoria")
    private String clave;

    @Schema(description = "Identificador del rol (4 para CLIENTE)", example = "4", required = false)
    private Long idRol;
}
