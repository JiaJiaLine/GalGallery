package com.galgallery.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class GameVO {

    private Long id;

    private String name;

    private String originalName;

    private String coverUrl;

    private String description;

    private String developer;

    private LocalDate releaseDate;

    private Integer sortOrder;

    private Long imageCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
