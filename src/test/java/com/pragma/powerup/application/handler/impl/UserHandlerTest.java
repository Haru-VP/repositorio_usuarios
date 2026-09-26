package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.UserRequestDto;
import com.pragma.powerup.application.mapper.IUserRequestMapper;
import com.pragma.powerup.domain.api.IUserServicePort;
import com.pragma.powerup.domain.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private IUserRequestMapper userRequestMapper;

    @InjectMocks
    private UserHandler userHandler;

    private UserRequestDto userRequestDto;
    private UserModel userModel;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto(
                "Carlos",
                "Perez",
                "123456789",
                "+573001234567",
                LocalDate.of(1995, 5, 20),
                "carlos@restaurante.com",
                "claveSegura123"
        );

        userModel = new UserModel();
        userModel.setNombre("Carlos");
        userModel.setApellido("Perez");
        userModel.setDocumentoDeIdentidad("123456789");
        userModel.setCelular("+573001234567");
        userModel.setFechaNacimiento(LocalDate.of(1995, 5, 20));
        userModel.setCorreo("carlos@restaurante.com");
        userModel.setClave("claveSegura123");
    }

    @Test
    void guardarPropietario_debeMapearYLlamarAlPuertoDelDominio() {
        // Arrange
        when(userRequestMapper.toModel(userRequestDto)).thenReturn(userModel);

        // Act
        userHandler.guardarPropietario(userRequestDto);

        // Assert
        verify(userRequestMapper, times(1)).toModel(userRequestDto);
        verify(userServicePort, times(1)).guardarPropietario(userModel);
    }

    @Test
    void guardarEmpleado_debeMapearYLlamarAlPuertoDelDominio() {
        // Arrange
        com.pragma.powerup.application.dto.request.EmployeeRequestDto employeeRequestDto =
                new com.pragma.powerup.application.dto.request.EmployeeRequestDto(
                        "Juan",
                        "Castro",
                        "987654321",
                        "+573109876543",
                        null,
                        "juan@restaurante.com",
                        "claveEmpleado123",
                        3L
                );
        when(userRequestMapper.toModel(employeeRequestDto)).thenReturn(userModel);

        // Act
        userHandler.guardarEmpleado(employeeRequestDto);

        // Assert
        verify(userRequestMapper, times(1)).toModel(employeeRequestDto);
        verify(userServicePort, times(1)).guardarEmpleado(userModel);
    }
}
