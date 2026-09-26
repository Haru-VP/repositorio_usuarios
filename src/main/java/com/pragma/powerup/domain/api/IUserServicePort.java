package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.UserModel;

public interface IUserServicePort {
    void guardarPropietario(UserModel userModel);
    void guardarEmpleado(UserModel userModel);
    UserModel obtenerUsuarioPorId(Long id);
}
