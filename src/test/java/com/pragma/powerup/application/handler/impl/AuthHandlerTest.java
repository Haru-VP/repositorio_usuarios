package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.LoginRequestDto;
import com.pragma.powerup.application.dto.response.JwtResponseDto;
import com.pragma.powerup.domain.api.IAuthServicePort;
import com.pragma.powerup.domain.model.AuthModel;
import com.pragma.powerup.domain.model.JwtTokenModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthHandlerTest {

    @Mock
    private IAuthServicePort authServicePort;

    @InjectMocks
    private AuthHandler authHandler;

    @Test
    void login_debeMapearYRetornarTokenCorrectamente() {
        // Arrange
        LoginRequestDto requestDto = new LoginRequestDto("admin@plazoleta.com", "1234");
        when(authServicePort.login(any(AuthModel.class))).thenReturn(new JwtTokenModel("token.prueba.jwt"));

        // Act
        JwtResponseDto responseDto = authHandler.login(requestDto);

        // Assert
        assertNotNull(responseDto);
        assertEquals("token.prueba.jwt", responseDto.getToken());

        ArgumentCaptor<AuthModel> captor = ArgumentCaptor.forClass(AuthModel.class);
        verify(authServicePort).login(captor.capture());
        assertEquals("admin@plazoleta.com", captor.getValue().getCorreo());
        assertEquals("1234", captor.getValue().getClave());
    }
}
