package com.bookmyevent.service.impl;

import com.bookmyevent.dto.request.TicketTierRequestDTO;
import com.bookmyevent.dto.response.TicketTierResponseDTO;
import com.bookmyevent.entity.Event;
import com.bookmyevent.entity.TicketTier;
import com.bookmyevent.exception.ResourceNotFoundException;
import com.bookmyevent.repository.EventRepository;
import com.bookmyevent.repository.TicketTierRepository;
import com.bookmyevent.service.TicketTierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketTierServiceImpl implements TicketTierService {

    private final TicketTierRepository ticketTierRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public TicketTierResponseDTO createTicketTier(Long eventId, TicketTierRequestDTO dto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        TicketTier tier = new TicketTier();
        tier.setEvent(event);
        tier.setName(dto.getName());
        tier.setPrice(dto.getPrice());
        tier.setCapacity(dto.getCapacity());
        tier.setAvailableQty(dto.getCapacity()); // Initially, available == capacity

        return mapToResponse(ticketTierRepository.save(tier));
    }

    @Override
    @Transactional
    public TicketTierResponseDTO updateTicketTier(Long tierId, TicketTierRequestDTO dto) {
        TicketTier tier = ticketTierRepository.findById(tierId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket tier not found"));

        int capacityDiff = dto.getCapacity() - tier.getCapacity();
        tier.setName(dto.getName());
        tier.setPrice(dto.getPrice());
        tier.setCapacity(dto.getCapacity());
        tier.setAvailableQty(tier.getAvailableQty() + capacityDiff);

        return mapToResponse(ticketTierRepository.save(tier));
    }

    @Override
    public List<TicketTierResponseDTO> getTiersForEvent(Long eventId) {
        return ticketTierRepository.findByEventId(eventId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TicketTierResponseDTO mapToResponse(TicketTier tier) {
        TicketTierResponseDTO dto = new TicketTierResponseDTO();
        dto.setId(tier.getId());
        dto.setEventId(tier.getEvent().getId());
        dto.setName(tier.getName());
        dto.setPrice(tier.getPrice());
        dto.setCapacity(tier.getCapacity());
        dto.setAvailableQty(tier.getAvailableQty());
        return dto;
    }
}
