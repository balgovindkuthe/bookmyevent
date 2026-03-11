package com.bookmyevent.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckInRequestDTO {

    @NotBlank(message = "QR Code UUID cannot be blank")
    private String qrCodeUuid;
}
