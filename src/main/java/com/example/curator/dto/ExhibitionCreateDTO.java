package com.example.curator.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionCreateDTO {
    @NotNull(message = "Title cannot be null")
    @Size(min = 1, max = 80, message = "Title must be between 1 and 80 characters")
    private String title;

}
