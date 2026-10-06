package com.pragma.powerup.domain.spi;

public interface ITokenPersistencePort {

    String generarToken(Long id, String correo, String rol);
}
