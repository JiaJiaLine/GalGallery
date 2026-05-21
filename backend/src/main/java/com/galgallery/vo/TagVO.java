package com.galgallery.vo;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TagVO {

    private Long id;

    private String name;

    private String color;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
