package com.bookmyevent.service;

import com.bookmyevent.dto.request.EventRequestDTO;
import com.bookmyevent.dto.response.EventResponseDTO;
import org.springframework.data.domain.Page;

public interface EventService {

    EventResponseDTO createEvent(Long organizerId, EventRequestDTO eventRequestDTO);

    EventResponseDTO updateEvent(Long eventId, EventRequestDTO eventRequestDTO);

    void deleteEvent(Long eventId);

    EventResponseDTO publishEvent(Long eventId);

    EventResponseDTO getEventById(Long eventId);

    Page<EventResponseDTO> getAllEvents(int page, int size, String sortBy, String sortDir);

}