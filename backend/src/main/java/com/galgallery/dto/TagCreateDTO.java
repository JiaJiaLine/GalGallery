package com.galgallery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TagCreateDTO {

    @NotBlank(message = "Tag name is required")
    @Size(max = 50, message = "Tag name must be at most 50 characters")
    private String name;

    @Pattern(regexp = "^$|^#[0-9a-fA-F]{6}$", message = "Color must be empty or a hex color like #409EFF")
    private String color;
}
