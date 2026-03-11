package com.bookmyevent.service;

import com.bookmyevent.dto.request.AuthRequestDTO;
import com.bookmyevent.dto.request.RegisterRequestDTO;
import com.bookmyevent.dto.response.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO request);

    AuthResponseDTO authenticate(AuthRequestDTO request);
}
