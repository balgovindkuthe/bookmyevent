package com.bookmyevent.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckInResponseDTO {
    private String status;
    private String message;
    private Long ticketId;
    private String customerName;
    private LocalDateTime scannedAt;
}
