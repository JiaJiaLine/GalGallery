package com.galgallery.vo;

import lombok.Data;

@Data
public class GameCardVO {

    private Long id;

    private String name;

    private String originalName;

    private String coverUrl;

    private String description;

    private Long imageCount;
}
