package com.galgallery.controller;

import com.galgallery.common.PageResult;
import com.galgallery.common.Result;
import com.galgallery.dto.ImageQueryDTO;
import com.galgallery.dto.ImageUpdateDTO;
import com.galgallery.dto.ImageUploadDTO;
import com.galgallery.service.GalleryImageService;
import com.galgallery.vo.ImageCardVO;
import com.galgallery.vo.ImageVO;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Gallery image APIs for querying, uploading, updating, and deleting image records.
 */
@Validated
@RestController
@RequestMapping("/api/images")
public class GalleryImageController {

    private final GalleryImageService galleryImageService;

    public GalleryImageController(GalleryImageService galleryImageService) {
        this.galleryImageService = galleryImageService;
    }

    /**
     * Page images by game, type, and tags.
     */
    @GetMapping
    public Result<PageResult<ImageCardVO>> pageImages(@Valid @ModelAttribute ImageQueryDTO query) {
        return Result.success(galleryImageService.pageImages(query));
    }

    /**
     * Get one image by ID.
     */
    @GetMapping("/{id}")
    public Result<ImageVO> getImage(@PathVariable Long id) {
        return Result.success(galleryImageService.getImage(id));
    }

    /**
     * Upload one image file and create its database record.
     */
    @PostMapping("/upload")
    public Result<ImageVO> uploadImage(@RequestParam("file") MultipartFile file,
                                       @Valid @ModelAttribute ImageUploadDTO dto) {
        return Result.success(galleryImageService.uploadImage(file, dto));
    }

    /**
     * Update image metadata and tag bindings.
     */
    @PutMapping("/{id}")
    public Result<ImageVO> updateImage(@PathVariable Long id, @Valid @RequestBody ImageUpdateDTO dto) {
        return Result.success(galleryImageService.updateImage(id, dto));
    }

    /**
     * Logically delete an image and remove its tag relations.
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteImage(@PathVariable Long id) {
        galleryImageService.deleteImage(id);
        return Result.success();
    }
}
