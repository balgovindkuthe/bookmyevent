package com.bookmyevent.service;

import com.bookmyevent.dto.response.AnalyticsResponseDTO;

public interface AnalyticsService {
    AnalyticsResponseDTO getEventAnalytics(Long eventId, Long organizerId);
}
