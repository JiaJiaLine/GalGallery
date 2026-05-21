package com.galgallery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("gallery_image")
public class GalleryImage {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long gameId;

    private String type;

    private String title;

    private String imageUrl;

    private String thumbnailUrl;

    private String originalFilename;

    private Long fileSize;

    private Integer width;

    private Integer height;

    private String mimeType;

    private Integer sortOrder;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
