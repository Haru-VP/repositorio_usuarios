package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.AuthModel;
import com.pragma.powerup.domain.model.JwtTokenModel;

public interface IAuthServicePort {

    JwtTokenModel login(AuthModel authModel);
}
