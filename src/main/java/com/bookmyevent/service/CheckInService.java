package com.bookmyevent.service;

import com.bookmyevent.dto.request.CheckInRequestDTO;
import com.bookmyevent.dto.response.CheckInResponseDTO;

public interface CheckInService {
    CheckInResponseDTO handleScan(Long scannerId, CheckInRequestDTO checkInRequestDTO);
}
