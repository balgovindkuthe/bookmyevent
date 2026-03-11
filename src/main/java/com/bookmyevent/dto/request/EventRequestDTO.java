package com.bookmyevent.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRequestDTO {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String description;

    @NotBlank(message = "Location cannot be blank")
    private String location;

    @NotNull(message = "Event date must be provided")
    @FutureOrPresent(message = "Event date cannot be in the past")
    private LocalDateTime eventDate;

    @NotNull(message = "Capacity must be provided")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;
}
