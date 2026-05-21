package com.galgallery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@Data
public class GameCreateDTO {

    @NotBlank(message = "Game name is required")
    @Size(max = 100, message = "Game name must be at most 100 characters")
    private String name;

    @Size(max = 150, message = "Original name must be at most 150 characters")
    private String originalName;

    @Size(max = 255, message = "Cover URL must be at most 255 characters")
    private String coverUrl;

    private String description;

    @Size(max = 100, message = "Developer must be at most 100 characters")
    private String developer;

    private LocalDate releaseDate;

    private Integer sortOrder;
}
