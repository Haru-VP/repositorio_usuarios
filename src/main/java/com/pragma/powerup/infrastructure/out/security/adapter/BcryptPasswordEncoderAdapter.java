package com.pragma.powerup.infrastructure.out.security.adapter;

import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BcryptPasswordEncoderAdapter implements IPasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    public BcryptPasswordEncoderAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encriptarClave(String clave) {
        return passwordEncoder.encode(clave);
    }

    @Override
    public boolean verificarClave(String clavePlana, String claveEncriptada) {
        return passwordEncoder.matches(clavePlana, claveEncriptada);
    }
}
