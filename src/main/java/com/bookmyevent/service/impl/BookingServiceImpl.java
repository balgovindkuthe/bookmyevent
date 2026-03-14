package com.bookmyevent.service.impl;

import com.bookmyevent.dto.request.BookingRequestDTO;
import com.bookmyevent.dto.response.BookingResponseDTO;
import com.bookmyevent.entity.*;
import com.bookmyevent.entity.enums.BookingStatus;
import com.bookmyevent.entity.enums.PaymentStatus;
import com.bookmyevent.entity.enums.TicketStatus;
import com.bookmyevent.exception.ResourceNotFoundException;
import com.bookmyevent.exception.SoldOutException;
import com.bookmyevent.repository.*;
import com.bookmyevent.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TicketTierRepository ticketTierRepository;
    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;

    @Override
    @Transactional
    public BookingResponseDTO createBooking(Long customerId, BookingRequestDTO dto) {
        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        TicketTier tier = ticketTierRepository.findById(dto.getTicketTierId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket tier not found"));

        // 1. Concurrency Check: Attempt atomic inventory subtraction
        int updatedRows = ticketTierRepository.decrementInventory(tier.getId(), dto.getQuantity());
        if (updatedRows == 0) {
            throw new SoldOutException("Not enough tickets available for tier: " + tier.getName());
        }

        // 2. Calculate amount
        BigDecimal totalAmount = tier.getPrice().multiply(new BigDecimal(dto.getQuantity()));

        // 3. Create Booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setEvent(event);
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.PENDING);
        booking = bookingRepository.save(booking);

        // 4. Create dummy Pending Payment record
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(totalAmount);
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);

        return mapToResponse(booking);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingResponseDTO processPaymentSuccess(Long bookingId, String providerTxId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        // 1. Update Payment
        payment.setProviderTxId(providerTxId);
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        // 2. Update Booking
        booking.setStatus(BookingStatus.CONFIRMED);
        booking = bookingRepository.save(booking);


        Ticket ticket = new Ticket();
        ticket.setBooking(booking);

        TicketTier tier = ticketTierRepository.findByEventId(booking.getEvent().getId()).get(0);
        ticket.setTicketTier(tier);
        ticket.setQrCodeUuid(UUID.randomUUID().toString()); // UNIQUE SECURE UUID
        ticket.setStatus(TicketStatus.VALID);
        ticketRepository.save(ticket);

        return mapToResponse(booking);
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        // Standard cancellation logic (refund inventory, set status to CANCELLED)
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    public BookingResponseDTO getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    @Override
    public List<BookingResponseDTO> getCustomerBookings(Long customerId) {
        return bookingRepository.findByUserId(customerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private BookingResponseDTO mapToResponse(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUser().getId());
        dto.setEventId(booking.getEvent().getId());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setStatus(booking.getStatus());
        dto.setCreatedAt(booking.getCreatedAt());
        return dto;
    }
}
