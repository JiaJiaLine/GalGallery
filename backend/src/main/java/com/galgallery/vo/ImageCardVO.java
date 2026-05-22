package com.galgallery.vo;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class ImageCardVO {

    private Long id;

    private Long gameId;

    private String type;

    private String title;

    private String description;

    private String imageUrl;

    private String thumbnailUrl;

    private Integer width;

    private Integer height;

    private Integer sortOrder;

    private List<TagVO> tags;

    private LocalDateTime createdAt;
}
