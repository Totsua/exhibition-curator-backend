package com.example.curator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiArtworkIdDTO {
    @NotNull(message = "artId cannot be null")
    @Range(min= 0, message = "artId cannot be lower than 0")
    Long artId;
    @NotNull(message = "apiOrigin cannot be null")
    @NotBlank(message = "apiOrigin cannot be empty")
    String apiOrigin;
}
