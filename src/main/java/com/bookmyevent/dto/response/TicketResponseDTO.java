package com.bookmyevent.dto.response;

import com.bookmyevent.entity.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDTO {
    private Long id;
    private Long bookingId;
    private Long ticketTierId;
    private String qrCodeUuid;
    private TicketStatus status;
}
