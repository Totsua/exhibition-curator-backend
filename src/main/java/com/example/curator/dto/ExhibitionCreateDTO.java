package com.example.curator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionCreateDTO {
    @NotBlank(message = "Title cannot be empty")
    @Size(min = 1, max = 80, message = "Title must be between 1 and 80 characters")
    private String title;

}
