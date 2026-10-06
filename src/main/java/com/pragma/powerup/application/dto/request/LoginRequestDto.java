package com.pragma.powerup.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Credenciales para iniciar sesión en la plataforma")
public class LoginRequestDto {

    @Schema(description = "Correo electrónico del usuario", example = "admin@plazoleta.com")
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato de correo no es válido")
    private String correo;

    @Schema(description = "Contraseña en texto plano", example = "1234")
    @NotBlank(message = "La clave es obligatoria")
    private String clave;
}
