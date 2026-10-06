package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.ClientRequestDto;
import com.pragma.powerup.application.dto.request.EmployeeRequestDto;
import com.pragma.powerup.application.dto.request.UserRequestDto;
import com.pragma.powerup.application.dto.response.UserResponseDto;

public interface IUserHandler {

    void guardarPropietario(UserRequestDto userRequestDto);

    void guardarEmpleado(EmployeeRequestDto employeeRequestDto);

    void guardarCliente(ClientRequestDto clientRequestDto);

    UserResponseDto obtenerUsuarioPorId(Long id);
}
