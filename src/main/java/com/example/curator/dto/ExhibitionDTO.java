package com.example.curator.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionDTO {
    private Long id;

    @NotNull(message = "Title cannot be empty")
    @Size(min = 1, max = 80, message = "Title must be between 1 and 80 characters")
    private String title;

    private String description;
    private List<ArtworkDTO> artworks;
}
