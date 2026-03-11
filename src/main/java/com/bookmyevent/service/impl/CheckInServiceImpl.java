package com.bookmyevent.service.impl;

import com.bookmyevent.dto.request.CheckInRequestDTO;
import com.bookmyevent.dto.response.CheckInResponseDTO;
import com.bookmyevent.entity.CheckIn;
import com.bookmyevent.entity.Ticket;
import com.bookmyevent.entity.User;
import com.bookmyevent.entity.enums.TicketStatus;
import com.bookmyevent.exception.ResourceNotFoundException;
import com.bookmyevent.exception.TicketAlreadyUsedException;
import com.bookmyevent.repository.CheckInRepository;
import com.bookmyevent.repository.TicketRepository;
import com.bookmyevent.repository.UserRepository;
import com.bookmyevent.service.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckInServiceImpl implements CheckInService {

    private final TicketRepository ticketRepository;
    private final CheckInRepository checkInRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CheckInResponseDTO handleScan(Long scannerId, CheckInRequestDTO dto) {
        User scanner = userRepository.findById(scannerId)
                .orElseThrow(() -> new ResourceNotFoundException("Scanner not found"));

        Ticket ticket = ticketRepository.findByQrCodeUuid(dto.getQrCodeUuid())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Ticket QR Code"));

        if (ticket.getStatus() == TicketStatus.CHECKED_IN) {
            throw new TicketAlreadyUsedException("This ticket has already been used.");
        }

        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new TicketAlreadyUsedException("This ticket is cancelled.");
        }

        // Mark ticket as checked in
        ticket.setStatus(TicketStatus.CHECKED_IN);
        ticketRepository.save(ticket);

        // Record CheckIn audit log
        CheckIn checkIn = new CheckIn();
        checkIn.setTicket(ticket);
        checkIn.setScannedBy(scanner);
        checkInRepository.save(checkIn);

        // Return successfully
        CheckInResponseDTO response = new CheckInResponseDTO();
        response.setStatus("SUCCESS");
        response.setMessage("Ticket Validated Successfully!");
        response.setTicketId(ticket.getId());
        response.setCustomerName(ticket.getBooking().getUser().getName());
        response.setScannedAt(checkIn.getScannedAt());

        return response;
    }
}
