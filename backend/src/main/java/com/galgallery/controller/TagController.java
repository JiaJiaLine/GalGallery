package com.galgallery.controller;

import com.galgallery.common.PageResult;
import com.galgallery.common.Result;
import com.galgallery.dto.TagCreateDTO;
import com.galgallery.dto.TagQueryDTO;
import com.galgallery.dto.TagUpdateDTO;
import com.galgallery.service.TagService;
import com.galgallery.vo.TagVO;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Tag management APIs for querying and maintaining image tags.
 */
@Validated
@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Page tags by optional keyword.
     */
    @GetMapping
    public Result<PageResult<TagVO>> pageTags(@Valid @ModelAttribute TagQueryDTO query) {
        return Result.success(tagService.pageTags(query));
    }

    /**
     * List all tags.
     */
    @GetMapping("/all")
    public Result<List<TagVO>> listAllTags() {
        return Result.success(tagService.listAllTags());
    }

    /**
     * Get one tag by ID.
     */
    @GetMapping("/{id}")
    public Result<TagVO> getTag(@PathVariable Long id) {
        return Result.success(tagService.getTag(id));
    }

    /**
     * Create a new tag.
     */
    @PostMapping
    public Result<TagVO> createTag(@Valid @RequestBody TagCreateDTO dto) {
        return Result.success(tagService.createTag(dto));
    }

    /**
     * Update an existing tag.
     */
    @PutMapping("/{id}")
    public Result<TagVO> updateTag(@PathVariable Long id, @Valid @RequestBody TagUpdateDTO dto) {
        return Result.success(tagService.updateTag(id, dto));
    }

    /**
     * Delete an existing tag and its image relations.
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return Result.success();
    }
}
