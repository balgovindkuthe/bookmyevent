package com.bookmyevent.controller;

import com.bookmyevent.dto.response.AnalyticsResponseDTO;
import com.bookmyevent.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/events/{eventId}")
    public ResponseEntity<AnalyticsResponseDTO> getEventAnalytics(
            jakarta.servlet.http.HttpServletRequest request,
            @PathVariable Long eventId) {

        Long organizerId = Long.valueOf(request.getAttribute("userId").toString());
        return ResponseEntity.ok(analyticsService.getEventAnalytics(eventId, organizerId));
    }
}
