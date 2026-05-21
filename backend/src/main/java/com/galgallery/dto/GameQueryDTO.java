package com.galgallery.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class GameQueryDTO {

    private String keyword;

    private String type;

    @Min(value = 1, message = "Page must be greater than or equal to 1")
    private Long page = 1L;

    @Min(value = 1, message = "Size must be greater than or equal to 1")
    @Max(value = 100, message = "Size must be less than or equal to 100")
    private Long size = 12L;
}
