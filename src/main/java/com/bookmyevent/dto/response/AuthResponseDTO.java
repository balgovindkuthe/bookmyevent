package com.bookmyevent.dto.response;

import com.bookmyevent.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String token;
}
