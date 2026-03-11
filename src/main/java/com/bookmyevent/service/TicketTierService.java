package com.bookmyevent.service;

import com.bookmyevent.dto.request.TicketTierRequestDTO;
import com.bookmyevent.dto.response.TicketTierResponseDTO;

import java.util.List;

public interface TicketTierService {
    TicketTierResponseDTO createTicketTier(Long eventId, TicketTierRequestDTO requestDTO);

    TicketTierResponseDTO updateTicketTier(Long tierId, TicketTierRequestDTO requestDTO);

    List<TicketTierResponseDTO> getTiersForEvent(Long eventId);
}
