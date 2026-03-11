package com.bookmyevent.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsResponseDTO {
    private Long eventId;
    private String eventTitle;
    private BigDecimal totalRevenue;
    private Long totalCheckIns;
    private Map<String, Long> ticketsSoldPerTier;
}
