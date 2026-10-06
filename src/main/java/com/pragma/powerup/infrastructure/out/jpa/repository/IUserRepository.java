package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.infrastructure.out.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByCorreo(String correo);
    Boolean existsByDocumentoDeIdentidad(String documentoDeIdentidad);
    Optional<UserEntity> findByCorreo(String correo);
}
