package com.example.curator.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExhibitionPatchDTO {
    @Size(min = 1, max = 80, message = "Title must be between 1 and 80 characters")
    private String title;
    @Size(max = 500, message = "Description must below 500 characters")
    private String description;
}
