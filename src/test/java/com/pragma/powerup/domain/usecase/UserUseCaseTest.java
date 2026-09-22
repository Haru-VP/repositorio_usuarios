package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.CorreoYaExisteException;
import com.pragma.powerup.domain.exception.DocumentoInvalidoException;
import com.pragma.powerup.domain.exception.DocumentoYaExisteException;
import com.pragma.powerup.domain.exception.FormatoCelularInvalidoException;
import com.pragma.powerup.domain.exception.FormatoCorreoInvalidoException;
import com.pragma.powerup.domain.exception.RolNoEncontradoException;
import com.pragma.powerup.domain.exception.UsuarioMenorDeEdadException;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IRolePersistencePort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IRolePersistencePort rolePersistencePort;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private UserUseCase userUseCase;

    private UserModel usuarioValido;
    private RoleModel rolPropietario;

    @BeforeEach
    void setUp() {
        rolPropietario = new RoleModel(2L, "PROPIETARIO", "Rol de propietario de restaurante");

        usuarioValido = new UserModel();
        usuarioValido.setNombre("Carlos");
        usuarioValido.setApellido("Perez");
        usuarioValido.setDocumentoDeIdentidad("123456789");
        usuarioValido.setCelular("+573001234567");
        usuarioValido.setFechaNacimiento(LocalDate.now().minusYears(25));
        usuarioValido.setCorreo("carlos@restaurante.com");
        usuarioValido.setClave("claveSecreta123");
    }

    @Test
    void guardarPropietario_exitoso() {
        // Arrange (Preparación)
        when(userPersistencePort.existePorCorreo(anyString())).thenReturn(false);
        when(userPersistencePort.existePorDocumentoDeIdentidad(anyString())).thenReturn(false);
        when(rolePersistencePort.obtenerRolPorId(2L)).thenReturn(rolPropietario);
        when(passwordEncoderPort.encriptarClave("claveSecreta123")).thenReturn("claveEncriptadaBcrypt");

        // Act (Ejecución)
        userUseCase.guardarPropietario(usuarioValido);

        // Assert (Verificación)
        assertEquals("claveEncriptadaBcrypt", usuarioValido.getClave());
        assertNotNull(usuarioValido.getRol());
        assertEquals(2L, usuarioValido.getRol().getId());
        assertEquals("PROPIETARIO", usuarioValido.getRol().getNombre());
        verify(userPersistencePort, times(1)).guardarUsuario(usuarioValido);
    }

    @Test
    void guardarPropietario_usuarioMenorDeEdad_lanzaExcepcion() {
        // Arrange: Usuario con 17 años
        usuarioValido.setFechaNacimiento(LocalDate.now().minusYears(17));

        // Act & Assert
        assertThrows(UsuarioMenorDeEdadException.class, () -> userUseCase.guardarPropietario(usuarioValido));
        verify(userPersistencePort, never()).guardarUsuario(any());
    }

    @Test
    void guardarPropietario_documentoInvalidoConLetras_lanzaExcepcion() {
        // Arrange: Documento alfanumérico
        usuarioValido.setDocumentoDeIdentidad("12345ABC");

        // Act & Assert
        assertThrows(DocumentoInvalidoException.class, () -> userUseCase.guardarPropietario(usuarioValido));
        verify(userPersistencePort, never()).guardarUsuario(any());
    }

    @Test
    void guardarPropietario_celularInvalidoMayorA13Caracteres_lanzaExcepcion() {
        // Arrange: Celular con más de 13 caracteres
        usuarioValido.setCelular("+57300123456789");

        // Act & Assert
        assertThrows(FormatoCelularInvalidoException.class, () -> userUseCase.guardarPropietario(usuarioValido));
        verify(userPersistencePort, never()).guardarUsuario(any());
    }

    @Test
    void guardarPropietario_correoInvalido_lanzaExcepcion() {
        // Arrange: Correo sin formato de email
        usuarioValido.setCorreo("correo-invalido.com");

        // Act & Assert
        assertThrows(FormatoCorreoInvalidoException.class, () -> userUseCase.guardarPropietario(usuarioValido));
        verify(userPersistencePort, never()).guardarUsuario(any());
    }

    @Test
    void guardarPropietario_correoYaExiste_lanzaExcepcion() {
        // Arrange
        when(userPersistencePort.existePorCorreo("carlos@restaurante.com")).thenReturn(true);

        // Act & Assert
        assertThrows(CorreoYaExisteException.class, () -> userUseCase.guardarPropietario(usuarioValido));
        verify(userPersistencePort, never()).guardarUsuario(any());
    }

    @Test
    void guardarPropietario_documentoYaExiste_lanzaExcepcion() {
        // Arrange
        when(userPersistencePort.existePorCorreo(anyString())).thenReturn(false);
        when(userPersistencePort.existePorDocumentoDeIdentidad("123456789")).thenReturn(true);

        // Act & Assert
        assertThrows(DocumentoYaExisteException.class, () -> userUseCase.guardarPropietario(usuarioValido));
        verify(userPersistencePort, never()).guardarUsuario(any());
    }

    @Test
    void guardarPropietario_rolNoEncontrado_lanzaExcepcion() {
        // Arrange: Base de datos no tiene configurado el rol con ID 2
        when(userPersistencePort.existePorCorreo(anyString())).thenReturn(false);
        when(userPersistencePort.existePorDocumentoDeIdentidad(anyString())).thenReturn(false);
        when(rolePersistencePort.obtenerRolPorId(2L)).thenReturn(null);

        // Act & Assert
        assertThrows(RolNoEncontradoException.class, () -> userUseCase.guardarPropietario(usuarioValido));
        verify(userPersistencePort, never()).guardarUsuario(any());
    }
}
