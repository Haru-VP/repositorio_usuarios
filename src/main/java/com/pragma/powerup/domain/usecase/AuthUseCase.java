package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IAuthServicePort;
import com.pragma.powerup.domain.exception.CredencialesInvalidasException;
import com.pragma.powerup.domain.model.AuthModel;
import com.pragma.powerup.domain.model.JwtTokenModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.ITokenPersistencePort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;

public class AuthUseCase implements IAuthServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;
    private final ITokenPersistencePort tokenPersistencePort;

    public AuthUseCase(IUserPersistencePort userPersistencePort,
                       IPasswordEncoderPort passwordEncoderPort,
                       ITokenPersistencePort tokenPersistencePort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenPersistencePort = tokenPersistencePort;
    }

    @Override
    public JwtTokenModel login(AuthModel authModel) {
        if (authModel == null || authModel.getCorreo() == null || authModel.getClave() == null) {
            throw new CredencialesInvalidasException();
        }

        UserModel user = userPersistencePort.obtenerPorCorreo(authModel.getCorreo());
        if (user == null) {
            throw new CredencialesInvalidasException();
        }

        boolean esClaveValida = passwordEncoderPort.verificarClave(authModel.getClave(), user.getClave());
        if (!esClaveValida) {
            throw new CredencialesInvalidasException();
        }

        String rol = user.getRol() != null ? user.getRol().getNombre() : "";
        String token = tokenPersistencePort.generarToken(user.getId(), user.getCorreo(), rol);

        return new JwtTokenModel(token);
    }
}
