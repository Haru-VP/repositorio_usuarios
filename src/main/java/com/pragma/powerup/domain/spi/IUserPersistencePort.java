package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.UserModel;

public interface IUserPersistencePort {
    void guardarUsuario(UserModel userModel);
    boolean existePorCorreo(String correo);
    boolean existePorDocumentoDeIdentidad(String documentoDeIdentidad);
    UserModel obtenerPorCorreo(String correo);
    UserModel obtenerPorId(Long id);
}
