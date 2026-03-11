package com.bookmyevent.service.impl;

import com.bookmyevent.dto.response.AnalyticsResponseDTO;
import com.bookmyevent.entity.Event;
import com.bookmyevent.exception.ResourceNotFoundException;
import com.bookmyevent.exception.UnauthorizedAccessException;
import com.bookmyevent.repository.AnalyticsRepository;
import com.bookmyevent.repository.EventRepository;
import com.bookmyevent.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsRepository analyticsRepository;
    private final EventRepository eventRepository;

    @Override
    public AnalyticsResponseDTO getEventAnalytics(Long eventId, Long organizerId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw new UnauthorizedAccessException("You are not authorized to view analytics for this event.");
        }

        BigDecimal totalRevenue = analyticsRepository.calculateTotalRevenueForEvent(eventId);
        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }

        Long totalCheckIns = analyticsRepository.countCheckInsForEvent(eventId);
        if (totalCheckIns == null) {
            totalCheckIns = 0L;
        }

        List<Object[]> rawTierData = analyticsRepository.countTicketsSoldPerTier(eventId);
        Map<String, Long> tierData = new HashMap<>();
        for (Object[] row : rawTierData) {
            tierData.put((String) row[0], (Long) row[1]);
        }

        return AnalyticsResponseDTO.builder()
                .eventId(event.getId())
                .eventTitle(event.getTitle())
                .totalRevenue(totalRevenue)
                .totalCheckIns(totalCheckIns)
                .ticketsSoldPerTier(tierData)
                .build();
    }
}
