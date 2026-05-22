package com.galgallery.vo;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class ImageVO {

    private Long id;

    private Long gameId;

    private String type;

    private String title;

    private String description;

    private String imageUrl;

    private String thumbnailUrl;

    private String originalFilename;

    private Long fileSize;

    private Integer width;

    private Integer height;

    private String mimeType;

    private Integer sortOrder;

    private List<TagVO> tags;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
