package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.UserEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IUserEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    @Override
    public void guardarUsuario(UserModel userModel) {
        UserEntity userEntity = userEntityMapper.toEntity(userModel);
        userRepository.save(userEntity);
    }

    @Override
    public boolean existePorCorreo(String correo) {
        return userRepository.existsByCorreo(correo);
    }

    @Override
    public boolean existePorDocumentoDeIdentidad(String documentoDeIdentidad) {
        return userRepository.existsByDocumentoDeIdentidad(documentoDeIdentidad);
    }

    @Override
    public UserModel obtenerPorCorreo(String correo) {
        return userRepository.findByCorreo(correo)
                .map(userEntityMapper::toUserModel)
                .orElse(null);
    }

    @Override
    public UserModel obtenerPorId(Long id) {
        return userRepository.findById(id)
                .map(userEntityMapper::toUserModel)
                .orElse(null);
    }
}
