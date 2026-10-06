package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.CredencialesInvalidasException;
import com.pragma.powerup.domain.model.AuthModel;
import com.pragma.powerup.domain.model.JwtTokenModel;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.ITokenPersistencePort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    @Mock
    private ITokenPersistencePort tokenPersistencePort;

    @InjectMocks
    private AuthUseCase authUseCase;

    private UserModel userModel;
    private AuthModel authModelValido;

    @BeforeEach
    void setUp() {
        RoleModel roleModel = new RoleModel(1L, "ADMINISTRADOR", "Rol administrador");
        userModel = new UserModel(
                99L,
                "Admin",
                "Sistema",
                "1000000000",
                "+573000000000",
                LocalDate.of(1990, 1, 1),
                "admin@plazoleta.com",
                "$2a$10$hashedPassword",
                roleModel
        );

        authModelValido = new AuthModel("admin@plazoleta.com", "1234");
    }

    @Test
    void login_credencialesCorrectas_retornaJwtTokenModel() {
        // Arrange
        String tokenEsperado = "token.jwt.firmado";
        when(userPersistencePort.obtenerPorCorreo("admin@plazoleta.com")).thenReturn(userModel);
        when(passwordEncoderPort.verificarClave("1234", "$2a$10$hashedPassword")).thenReturn(true);
        when(tokenPersistencePort.generarToken(99L, "admin@plazoleta.com", "ADMINISTRADOR")).thenReturn(tokenEsperado);

        // Act
        JwtTokenModel resultado = authUseCase.login(authModelValido);

        // Assert
        assertNotNull(resultado);
        assertEquals(tokenEsperado, resultado.getToken());
        verify(userPersistencePort).obtenerPorCorreo("admin@plazoleta.com");
        verify(passwordEncoderPort).verificarClave("1234", "$2a$10$hashedPassword");
        verify(tokenPersistencePort).generarToken(99L, "admin@plazoleta.com", "ADMINISTRADOR");
    }

    @Test
    void login_usuarioNoExiste_lanzaCredencialesInvalidasException() {
        // Arrange
        when(userPersistencePort.obtenerPorCorreo("noexiste@plazoleta.com")).thenReturn(null);
        AuthModel authInexistente = new AuthModel("noexiste@plazoleta.com", "1234");

        // Act & Assert
        assertThrows(CredencialesInvalidasException.class, () -> authUseCase.login(authInexistente));
        verify(passwordEncoderPort, never()).verificarClave(anyString(), anyString());
        verify(tokenPersistencePort, never()).generarToken(anyLong(), anyString(), anyString());
    }

    @Test
    void login_claveIncorrecta_lanzaCredencialesInvalidasException() {
        // Arrange
        when(userPersistencePort.obtenerPorCorreo("admin@plazoleta.com")).thenReturn(userModel);
        when(passwordEncoderPort.verificarClave("claveErronea", "$2a$10$hashedPassword")).thenReturn(false);
        AuthModel authClaveErronea = new AuthModel("admin@plazoleta.com", "claveErronea");

        // Act & Assert
        assertThrows(CredencialesInvalidasException.class, () -> authUseCase.login(authClaveErronea));
        verify(tokenPersistencePort, never()).generarToken(anyLong(), anyString(), anyString());
    }

    @Test
    void login_datosNulosOIncompletos_lanzaCredencialesInvalidasException() {
        // Act & Assert
        assertThrows(CredencialesInvalidasException.class, () -> authUseCase.login(null));
        assertThrows(CredencialesInvalidasException.class, () -> authUseCase.login(new AuthModel(null, "1234")));
        assertThrows(CredencialesInvalidasException.class, () -> authUseCase.login(new AuthModel("admin@plazoleta.com", null)));
        verify(userPersistencePort, never()).obtenerPorCorreo(any());
    }
}
