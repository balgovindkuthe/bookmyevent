package com.bookmyevent.service.impl;

import com.bookmyevent.dto.request.EventRequestDTO;
import com.bookmyevent.dto.response.EventResponseDTO;
import com.bookmyevent.entity.Event;
import com.bookmyevent.entity.User;
import com.bookmyevent.entity.enums.EventStatus;
import com.bookmyevent.exception.ResourceNotFoundException;
import com.bookmyevent.repository.EventRepository;
import com.bookmyevent.repository.UserRepository;
import com.bookmyevent.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public EventResponseDTO createEvent(Long organizerId, EventRequestDTO dto) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new ResourceNotFoundException("Organizer not found"));

        Event newEvent = new Event();
        newEvent.setOrganizer(organizer);
        newEvent.setTitle(dto.getTitle());
        newEvent.setDescription(dto.getDescription());
        newEvent.setLocation(dto.getLocation());
        newEvent.setEventDate(dto.getEventDate());
        newEvent.setCapacity(dto.getCapacity());
        newEvent.setStatus(EventStatus.DRAFT);

        newEvent = eventRepository.save(newEvent);
        return mapToResponse(newEvent);
    }

    @Override
    @Transactional
    public EventResponseDTO updateEvent(Long eventId, EventRequestDTO dto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());
        event.setEventDate(dto.getEventDate());
        event.setCapacity(dto.getCapacity());

        return mapToResponse(eventRepository.save(event));
    }

    @Override
    @Transactional
    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        event.setStatus(EventStatus.CANCELLED);
        eventRepository.save(event);
    }

    @Override
    @Transactional
    public EventResponseDTO publishEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        event.setStatus(EventStatus.PUBLISHED);
        return mapToResponse(eventRepository.save(event));
    }

    @Override
    public EventResponseDTO getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        return mapToResponse(event);
    }

    // Pagination

    @Override
    public Page<EventResponseDTO> getAllEvents(int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Event> events = eventRepository.findAll(pageable);

        return events.map(this::mapToResponse);
    }

    private EventResponseDTO mapToResponse(Event event) {
        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setOrganizerId(event.getOrganizer().getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setLocation(event.getLocation());
        dto.setEventDate(event.getEventDate());
        dto.setCapacity(event.getCapacity());
        dto.setStatus(event.getStatus());
        dto.setCreatedAt(event.getCreatedAt());
        return dto;
    }
}