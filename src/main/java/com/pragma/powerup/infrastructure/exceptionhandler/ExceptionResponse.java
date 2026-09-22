package com.pragma.powerup.infrastructure.exceptionhandler;

public enum ExceptionResponse {
    NO_DATA_FOUND("No data found for the requested petition"),
    USUARIO_MENOR_DE_EDAD("El usuario debe ser mayor de edad para registrarse como propietario"),
    DOCUMENTO_INVALIDO("El documento de identidad debe ser únicamente numérico"),
    FORMATO_CELULAR_INVALIDO("El formato del celular es inválido o supera los 13 caracteres"),
    FORMATO_CORREO_INVALIDO("El correo electrónico no tiene una estructura válida"),
    CORREO_YA_EXISTE("Ya existe un usuario registrado con ese correo electrónico"),
    DOCUMENTO_YA_EXISTE("Ya existe un usuario registrado con ese documento de identidad"),
    ROL_NO_ENCONTRADO("El rol especificado no existe en el sistema");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}