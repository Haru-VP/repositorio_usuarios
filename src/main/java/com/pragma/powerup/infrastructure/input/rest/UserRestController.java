package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.UserRequestDto;
import com.pragma.powerup.application.handler.IUserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserRestController {

    private final IUserHandler userHandler;

    @Operation(summary = "Crear un nuevo usuario con rol de propietario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Propietario creado exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o reglas de negocio no cumplidas", content = @Content),
            @ApiResponse(responseCode = "404", description = "Rol de propietario no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "El usuario ya existe por correo o documento de identidad", content = @Content)
    })
    @PostMapping("/propietario")
    public ResponseEntity<Void> guardarPropietario(@Valid @RequestBody UserRequestDto userRequestDto) {
        userHandler.guardarPropietario(userRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
