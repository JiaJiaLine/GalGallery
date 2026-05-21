package com.galgallery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.galgallery.common.PageResult;
import com.galgallery.dto.TagCreateDTO;
import com.galgallery.dto.TagQueryDTO;
import com.galgallery.dto.TagUpdateDTO;
import com.galgallery.entity.Tag;
import com.galgallery.vo.TagVO;
import java.util.List;

public interface TagService extends IService<Tag> {

    PageResult<TagVO> pageTags(TagQueryDTO query);

    List<TagVO> listAllTags();

    TagVO getTag(Long id);

    TagVO createTag(TagCreateDTO dto);

    TagVO updateTag(Long id, TagUpdateDTO dto);

    void deleteTag(Long id);
}
