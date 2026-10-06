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
@Schema(description = "Respuesta de autenticación con token de acceso JWT")
public class JwtResponseDto {

    @Schema(description = "Token JWT firmado")
    private String token;
}
