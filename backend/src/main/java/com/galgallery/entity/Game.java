package com.galgallery.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("game")
public class Game {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String originalName;

    private String coverUrl;

    private String description;

    private String developer;

    private LocalDate releaseDate;

    private Integer sortOrder;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
