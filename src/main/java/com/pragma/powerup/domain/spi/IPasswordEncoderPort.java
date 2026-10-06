package com.pragma.powerup.domain.spi;

public interface IPasswordEncoderPort {
    String encriptarClave(String clave);
    boolean verificarClave(String clavePlana, String claveEncriptada);
}
