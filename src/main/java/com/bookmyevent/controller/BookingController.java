package com.bookmyevent.controller;

import com.bookmyevent.dto.request.BookingRequestDTO;
import com.bookmyevent.dto.response.BookingResponseDTO;
import com.bookmyevent.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(
            jakarta.servlet.http.HttpServletRequest request,
            @Valid @RequestBody BookingRequestDTO requestDTO) {
        Long customerId = (Long) request.getAttribute("userId");
        return new ResponseEntity<>(bookingService.createBooking(customerId, requestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/{bookingId}/payment-success")
    public ResponseEntity<BookingResponseDTO> markPaymentSuccess(
            @PathVariable Long bookingId,
            @RequestParam String providerTxId) {
        return ResponseEntity.ok(bookingService.processPaymentSuccess(bookingId, providerTxId));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.getBookingById(bookingId));
    }

    @GetMapping("/customer/me")
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings(jakarta.servlet.http.HttpServletRequest request) {
        Long customerId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(bookingService.getCustomerBookings(customerId));
    }
}
