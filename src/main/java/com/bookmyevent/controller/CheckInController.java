package com.bookmyevent.controller;

import com.bookmyevent.dto.request.CheckInRequestDTO;
import com.bookmyevent.dto.response.CheckInResponseDTO;
import com.bookmyevent.service.CheckInService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checkin")
@RequiredArgsConstructor
public class CheckInController {

    private final CheckInService checkInService;

    @PostMapping("/scan")
    public ResponseEntity<CheckInResponseDTO> handleScan(
            jakarta.servlet.http.HttpServletRequest request,
            @Valid @RequestBody CheckInRequestDTO requestDTO) {
        Long scannerId = Long.valueOf(request.getAttribute("userId").toString());
        return ResponseEntity.ok(checkInService.handleScan(scannerId, requestDTO));
    }
}
