package com.galgallery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.galgallery.entity.ImageTag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

public interface ImageTagMapper extends BaseMapper<ImageTag> {

    @Delete("DELETE FROM image_tag WHERE gallery_image_id = #{galleryImageId}")
    int deleteByGalleryImageIdPhysically(@Param("galleryImageId") Long galleryImageId);
}
