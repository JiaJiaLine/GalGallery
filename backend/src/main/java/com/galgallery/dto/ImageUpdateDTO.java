package com.galgallery.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ImageUpdateDTO {

    @Size(max = 100, message = "Title must be at most 100 characters")
    private String title;

    private String description;

    private String type;

    private Integer sortOrder;

    private String tagIds;
}
