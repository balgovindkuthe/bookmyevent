package com.bookmyevent.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequestDTO {

    @NotNull(message = "Event ID cannot be null")
    private Long eventId;

    @NotNull(message = "Ticket Tier ID cannot be null")
    private Long ticketTierId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
