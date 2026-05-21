package com.galgallery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.galgallery.common.PageResult;
import com.galgallery.common.ResultCode;
import com.galgallery.dto.TagCreateDTO;
import com.galgallery.dto.TagQueryDTO;
import com.galgallery.dto.TagUpdateDTO;
import com.galgallery.entity.ImageTag;
import com.galgallery.entity.Tag;
import com.galgallery.exception.BusinessException;
import com.galgallery.mapper.ImageTagMapper;
import com.galgallery.mapper.TagMapper;
import com.galgallery.service.TagService;
import com.galgallery.vo.TagVO;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final ImageTagMapper imageTagMapper;

    public TagServiceImpl(ImageTagMapper imageTagMapper) {
        this.imageTagMapper = imageTagMapper;
    }

    @Override
    public PageResult<TagVO> pageTags(TagQueryDTO query) {
        Page<Tag> page = new Page<>(normalizePage(query.getPage()), normalizeSize(query.getSize()));
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<Tag>()
                .like(StringUtils.hasText(query.getKeyword()), Tag::getName, query.getKeyword())
                .orderByAsc(Tag::getName)
                .orderByDesc(Tag::getCreatedAt)
                .orderByDesc(Tag::getId);

        Page<Tag> result = page(page, wrapper);
        List<TagVO> records = result.getRecords().stream()
                .map(this::toVO)
                .toList();
        return new PageResult<>(records, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public List<TagVO> listAllTags() {
        return list(new LambdaQueryWrapper<Tag>()
                .orderByAsc(Tag::getName)
                .orderByDesc(Tag::getCreatedAt)
                .orderByDesc(Tag::getId))
                .stream()
                .map(this::toVO)
                .toList();
    }

    @Override
    public TagVO getTag(Long id) {
        return toVO(requireTag(id));
    }

    @Override
    @Transactional
    public TagVO createTag(TagCreateDTO dto) {
        String name = normalizeName(dto.getName());
        ensureNameAvailable(name, null);

        Tag tag = new Tag();
        tag.setName(name);
        tag.setColor(normalizeColor(dto.getColor()));
        save(tag);
        return getTag(tag.getId());
    }

    @Override
    @Transactional
    public TagVO updateTag(Long id, TagUpdateDTO dto) {
        Tag tag = requireTag(id);
        String name = normalizeName(dto.getName());
        ensureNameAvailable(name, id);

        tag.setName(name);
        tag.setColor(normalizeColor(dto.getColor()));
        updateById(tag);
        return getTag(id);
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        requireTag(id);
        imageTagMapper.delete(new LambdaQueryWrapper<ImageTag>().eq(ImageTag::getTagId, id));
        removeById(id);
    }

    private Tag requireTag(Long id) {
        Tag tag = getById(id);
        if (tag == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "Tag not found");
        }
        return tag;
    }

    private void ensureNameAvailable(String name, Long currentId) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<Tag>()
                .eq(Tag::getName, name)
                .ne(currentId != null, Tag::getId, currentId);
        if (count(wrapper) > 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "Tag name already exists");
        }
    }

    private String normalizeName(String name) {
        return name == null ? null : name.trim();
    }

    private String normalizeColor(String color) {
        return StringUtils.hasText(color) ? color.trim() : null;
    }

    private Long normalizePage(Long page) {
        return page == null || page < 1 ? 1L : page;
    }

    private Long normalizeSize(Long size) {
        if (size == null || size < 1) {
            return 10L;
        }
        return Math.min(size, 100L);
    }

    private TagVO toVO(Tag tag) {
        TagVO vo = new TagVO();
        vo.setId(tag.getId());
        vo.setName(tag.getName());
        vo.setColor(tag.getColor());
        vo.setCreatedAt(tag.getCreatedAt());
        vo.setUpdatedAt(tag.getUpdatedAt());
        return vo;
    }
}
