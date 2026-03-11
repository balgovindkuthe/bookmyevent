package com.bookmyevent.service;

import com.bookmyevent.dto.request.BookingRequestDTO;
import com.bookmyevent.dto.response.BookingResponseDTO;

import java.util.List;

public interface BookingService {
    BookingResponseDTO createBooking(Long customerId, BookingRequestDTO dto);

    BookingResponseDTO processPaymentSuccess(Long bookingId, String providerTxId);

    void cancelBooking(Long bookingId);

    BookingResponseDTO getBookingById(Long bookingId);

    List<BookingResponseDTO> getCustomerBookings(Long customerId);
}
