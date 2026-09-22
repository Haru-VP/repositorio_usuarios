package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IUserServicePort;
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

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class UserUseCase implements IUserServicePort {

    private static final Long ID_ROL_PROPIETARIO = 2L;
    private static final int MAYORIA_DE_EDAD = 18;
    private static final int LONGITUD_MAXIMA_CELULAR = 13;
    private static final Pattern PATRON_DOCUMENTO = Pattern.compile("^[0-9]+$");
    private static final Pattern PATRON_CELULAR = Pattern.compile("^\\+?[0-9]+$");
    private static final Pattern PATRON_CORREO = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private final IUserPersistencePort userPersistencePort;
    private final IRolePersistencePort rolePersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;

    public UserUseCase(IUserPersistencePort userPersistencePort,
                       IRolePersistencePort rolePersistencePort,
                       IPasswordEncoderPort passwordEncoderPort) {
        this.userPersistencePort = userPersistencePort;
        this.rolePersistencePort = rolePersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public void guardarPropietario(UserModel userModel) {
        validarMayorDeEdad(userModel.getFechaNacimiento());
        validarDocumento(userModel.getDocumentoDeIdentidad());
        validarCelular(userModel.getCelular());
        validarCorreo(userModel.getCorreo());
        validarUnicidad(userModel.getCorreo(), userModel.getDocumentoDeIdentidad());

        RoleModel rol = rolePersistencePort.obtenerRolPorId(ID_ROL_PROPIETARIO);
        if (rol == null) {
            throw new RolNoEncontradoException();
        }
        userModel.setRol(rol);

        userModel.setClave(passwordEncoderPort.encriptarClave(userModel.getClave()));
        userPersistencePort.guardarUsuario(userModel);
    }

    private void validarMayorDeEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null || Period.between(fechaNacimiento, LocalDate.now()).getYears() < MAYORIA_DE_EDAD) {
            throw new UsuarioMenorDeEdadException();
        }
    }

    private void validarDocumento(String documento) {
        if (documento == null || !PATRON_DOCUMENTO.matcher(documento).matches()) {
            throw new DocumentoInvalidoException();
        }
    }

    private void validarCelular(String celular) {
        if (celular == null || celular.length() > LONGITUD_MAXIMA_CELULAR || !PATRON_CELULAR.matcher(celular).matches()) {
            throw new FormatoCelularInvalidoException();
        }
    }

    private void validarCorreo(String correo) {
        if (correo == null || !PATRON_CORREO.matcher(correo).matches()) {
            throw new FormatoCorreoInvalidoException();
        }
    }

    private void validarUnicidad(String correo, String documento) {
        if (userPersistencePort.existePorCorreo(correo)) {
            throw new CorreoYaExisteException();
        }
        if (userPersistencePort.existePorDocumentoDeIdentidad(documento)) {
            throw new DocumentoYaExisteException();
        }
    }
}
