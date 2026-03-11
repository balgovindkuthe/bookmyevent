package com.bookmyevent.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketTierResponseDTO {
    private Long id;
    private Long eventId;
    private String name;
    private BigDecimal price;
    private Integer capacity;
    private Integer availableQty;
}
