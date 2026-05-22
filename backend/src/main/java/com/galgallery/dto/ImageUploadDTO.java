package com.galgallery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ImageUploadDTO {

    @NotNull(message = "Game ID is required")
    private Long gameId;

    @NotBlank(message = "Image type is required")
    private String type;

    @Size(max = 100, message = "Title must be at most 100 characters")
    private String title;

    private String description;

    private Integer sortOrder;

    private String tagIds;
}
