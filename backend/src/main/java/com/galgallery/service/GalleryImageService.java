package com.galgallery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.galgallery.common.PageResult;
import com.galgallery.dto.ImageQueryDTO;
import com.galgallery.dto.ImageUpdateDTO;
import com.galgallery.dto.ImageUploadDTO;
import com.galgallery.entity.GalleryImage;
import com.galgallery.vo.ImageCardVO;
import com.galgallery.vo.ImageVO;
import org.springframework.web.multipart.MultipartFile;

public interface GalleryImageService extends IService<GalleryImage> {

    PageResult<ImageCardVO> pageImages(ImageQueryDTO query);

    ImageVO getImage(Long id);

    ImageVO uploadImage(MultipartFile file, ImageUploadDTO dto);

    ImageVO updateImage(Long id, ImageUpdateDTO dto);

    void deleteImage(Long id);
}
