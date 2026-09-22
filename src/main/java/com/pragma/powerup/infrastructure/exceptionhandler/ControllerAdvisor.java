package com.pragma.powerup.infrastructure.exceptionhandler;

import com.pragma.powerup.domain.exception.CorreoYaExisteException;
import com.pragma.powerup.domain.exception.DocumentoInvalidoException;
import com.pragma.powerup.domain.exception.DocumentoYaExisteException;
import com.pragma.powerup.domain.exception.FormatoCelularInvalidoException;
import com.pragma.powerup.domain.exception.FormatoCorreoInvalidoException;
import com.pragma.powerup.domain.exception.RolNoEncontradoException;
import com.pragma.powerup.domain.exception.UsuarioMenorDeEdadException;
import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {

    private static final String MESSAGE = "message";

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoDataFoundException(
            NoDataFoundException ignoredNoDataFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.NO_DATA_FOUND.getMessage()));
    }

    @ExceptionHandler(UsuarioMenorDeEdadException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioMenorDeEdadException(
            UsuarioMenorDeEdadException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.USUARIO_MENOR_DE_EDAD.getMessage()));
    }

    @ExceptionHandler(DocumentoInvalidoException.class)
    public ResponseEntity<Map<String, String>> handleDocumentoInvalidoException(
            DocumentoInvalidoException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.DOCUMENTO_INVALIDO.getMessage()));
    }

    @ExceptionHandler(FormatoCelularInvalidoException.class)
    public ResponseEntity<Map<String, String>> handleFormatoCelularInvalidoException(
            FormatoCelularInvalidoException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.FORMATO_CELULAR_INVALIDO.getMessage()));
    }

    @ExceptionHandler(FormatoCorreoInvalidoException.class)
    public ResponseEntity<Map<String, String>> handleFormatoCorreoInvalidoException(
            FormatoCorreoInvalidoException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.FORMATO_CORREO_INVALIDO.getMessage()));
    }

    @ExceptionHandler(CorreoYaExisteException.class)
    public ResponseEntity<Map<String, String>> handleCorreoYaExisteException(
            CorreoYaExisteException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.CORREO_YA_EXISTE.getMessage()));
    }

    @ExceptionHandler(DocumentoYaExisteException.class)
    public ResponseEntity<Map<String, String>> handleDocumentoYaExisteException(
            DocumentoYaExisteException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.DOCUMENTO_YA_EXISTE.getMessage()));
    }

    @ExceptionHandler(RolNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleRolNoEncontradoException(
            RolNoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.ROL_NO_ENCONTRADO.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        String mensaje = exception.getBindingResult().getFieldError() != null
                ? exception.getBindingResult().getFieldError().getDefaultMessage()
                : "Datos de entrada inválidos";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, mensaje));
    }
}