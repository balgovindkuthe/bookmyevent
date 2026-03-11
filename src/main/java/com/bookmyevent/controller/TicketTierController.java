package com.bookmyevent.controller;

import com.bookmyevent.dto.request.TicketTierRequestDTO;
import com.bookmyevent.dto.response.TicketTierResponseDTO;
import com.bookmyevent.service.TicketTierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events/{eventId}/tiers")
@RequiredArgsConstructor
public class TicketTierController {

    private final TicketTierService ticketTierService;

    @PostMapping
    public ResponseEntity<TicketTierResponseDTO> createTicketTier(
            @PathVariable Long eventId,
            @Valid @RequestBody TicketTierRequestDTO requestDTO) {
        return new ResponseEntity<>(ticketTierService.createTicketTier(eventId, requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{tierId}")
    public ResponseEntity<TicketTierResponseDTO> updateTicketTier(
            @PathVariable Long eventId, // For path consistency
            @PathVariable Long tierId,
            @Valid @RequestBody TicketTierRequestDTO requestDTO) {
        return ResponseEntity.ok(ticketTierService.updateTicketTier(tierId, requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<TicketTierResponseDTO>> getTiersForEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(ticketTierService.getTiersForEvent(eventId));
    }
}
