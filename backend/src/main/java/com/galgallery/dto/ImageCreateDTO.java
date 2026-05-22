package com.galgallery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ImageCreateDTO {

    @NotNull(message = "Game ID is required")
    private Long gameId;

    @NotBlank(message = "Image type is required")
    private String type;

    @Size(max = 100, message = "Title must be at most 100 characters")
    private String title;

    private String description;

    @NotBlank(message = "Image URL is required")
    @Size(max = 255, message = "Image URL must be at most 255 characters")
    private String imageUrl;

    @Size(max = 255, message = "Thumbnail URL must be at most 255 characters")
    private String thumbnailUrl;

    @Size(max = 255, message = "Original filename must be at most 255 characters")
    private String originalFilename;

    private Long fileSize;

    private Integer width;

    private Integer height;

    @Size(max = 80, message = "MIME type must be at most 80 characters")
    private String mimeType;

    private Integer sortOrder;

    private String tagIds;
}
