package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.spi.IRolePersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRoleEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleJpaAdapter implements IRolePersistencePort {

    private final IRoleRepository roleRepository;
    private final IRoleEntityMapper roleEntityMapper;

    @Override
    public RoleModel obtenerRolPorId(Long id) {
        return roleRepository.findById(id)
                .map(roleEntityMapper::toRoleModel)
                .orElse(null);
    }

    @Override
    public RoleModel obtenerRolPorNombre(String nombre) {
        return roleRepository.findByNombre(nombre)
                .map(roleEntityMapper::toRoleModel)
                .orElse(null);
    }
}
