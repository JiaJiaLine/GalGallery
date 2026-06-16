package com.galgallery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.galgallery.common.PageResult;
import com.galgallery.common.ResultCode;
import com.galgallery.config.GalGalleryProperties;
import com.galgallery.dto.ImageQueryDTO;
import com.galgallery.dto.ImageUpdateDTO;
import com.galgallery.dto.ImageUploadDTO;
import com.galgallery.entity.GalleryImage;
import com.galgallery.entity.Game;
import com.galgallery.entity.ImageTag;
import com.galgallery.entity.Tag;
import com.galgallery.exception.BusinessException;
import com.galgallery.mapper.GalleryImageMapper;
import com.galgallery.mapper.GameMapper;
import com.galgallery.mapper.ImageTagMapper;
import com.galgallery.mapper.TagMapper;
import com.galgallery.service.GalleryImageService;
import com.galgallery.vo.ImageCardVO;
import com.galgallery.vo.ImageVO;
import com.galgallery.vo.TagVO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GalleryImageServiceImpl extends ServiceImpl<GalleryImageMapper, GalleryImage>
        implements GalleryImageService {

    private static final Set<String> IMAGE_TYPES = Set.of("character", "photo", "screenshot");
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
    private static final String IMAGE_ACCESS_PREFIX = "/uploads/images/";
    private static final String IMAGE_DIRECTORY = "images";

    private final GameMapper gameMapper;
    private final ImageTagMapper imageTagMapper;
    private final TagMapper tagMapper;
    private final Path imageUploadDir;

    public GalleryImageServiceImpl(GameMapper gameMapper, ImageTagMapper imageTagMapper, TagMapper tagMapper,
                                   GalGalleryProperties properties) {
        this.gameMapper = gameMapper;
        this.imageTagMapper = imageTagMapper;
        this.tagMapper = tagMapper;
        this.imageUploadDir = Path.of(properties.getUpload().getPath(), IMAGE_DIRECTORY);
    }

    @Override
    public PageResult<ImageCardVO> pageImages(ImageQueryDTO query) {
        String type = normalizeType(query.getType(), false);
        List<Long> tagIds = parseTagIds(query.getTagIds());
        List<Long> imageIds = findImageIdsByAllTags(tagIds);
        if (!tagIds.isEmpty() && imageIds.isEmpty()) {
            return new PageResult<>(List.of(), 0L, normalizePage(query.getPage()), normalizeSize(query.getSize()));
        }

        Page<GalleryImage> page = new Page<>(normalizePage(query.getPage()), normalizeSize(query.getSize()));
        LambdaQueryWrapper<GalleryImage> wrapper = new LambdaQueryWrapper<GalleryImage>()
                .eq(query.getGameId() != null, GalleryImage::getGameId, query.getGameId())
                .eq(StringUtils.hasText(type), GalleryImage::getType, type)
                .in(!imageIds.isEmpty(), GalleryImage::getId, imageIds)
                .orderByAsc(GalleryImage::getSortOrder)
                .orderByDesc(GalleryImage::getCreatedAt)
                .orderByDesc(GalleryImage::getId);

        Page<GalleryImage> result = page(page, wrapper);
        List<ImageCardVO> records = result.getRecords().stream()
                .map(image -> toCardVO(image, listTagsByImageId(image.getId())))
                .toList();
        return new PageResult<>(records, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public ImageVO getImage(Long id) {
        GalleryImage image = requireImage(id);
        return toVO(image, listTagsByImageId(id));
    }

    @Override
    @Transactional
    public ImageVO uploadImage(MultipartFile file, ImageUploadDTO dto) {
        validateFile(file);
        requireGame(dto.getGameId());
        String type = normalizeType(dto.getType(), true);
        List<Long> tagIds = parseTagIds(dto.getTagIds());
        ensureTagsExist(tagIds);

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() == null
                ? "image"
                : file.getOriginalFilename());
        String extension = getExtension(originalFilename);
        String storedFilename = UUID.randomUUID() + "." + extension;
        Path target = imageUploadDir.toAbsolutePath().normalize().resolve(storedFilename);
        try {
            Files.createDirectories(target.getParent());
            file.transferTo(target);
        } catch (IOException exception) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR.getCode(), "Failed to save uploaded image："+ exception.getMessage());
        }

        ImageSize imageSize = readImageSize(target);
        GalleryImage image = new GalleryImage();
        image.setGameId(dto.getGameId());
        image.setType(type);
        if (dto.getTitle() != null) {
            image.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            image.setDescription(dto.getDescription());
        }
        image.setImageUrl(IMAGE_ACCESS_PREFIX + storedFilename);
        image.setThumbnailUrl(null);
        image.setOriginalFilename(originalFilename);
        image.setFileSize(file.getSize());
        image.setWidth(imageSize.width());
        image.setHeight(imageSize.height());
        image.setMimeType(resolveMimeType(file.getContentType(), extension));
        image.setSortOrder(dto.getSortOrder() == null ? 0 : dto.getSortOrder());
        save(image);
        syncTags(image.getId(), tagIds);
        return getImage(image.getId());
    }

    @Override
    @Transactional
    public ImageVO updateImage(Long id, ImageUpdateDTO dto) {
        GalleryImage image = requireImage(id);
        String type = normalizeType(dto.getType(), false);
        List<Long> tagIds = parseTagIds(dto.getTagIds());
        if (dto.getTagIds() != null) {
            ensureTagsExist(tagIds);
        }

        if (dto.getTitle() != null) {
            image.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            image.setDescription(dto.getDescription());
        }
        if (StringUtils.hasText(type)) {
            image.setType(type);
        }
        if (dto.getSortOrder() != null) {
            image.setSortOrder(dto.getSortOrder());
        }
        updateById(image);
        if (dto.getTagIds() != null) {
            syncTags(id, tagIds);
        }
        return getImage(id);
    }

    @Override
    @Transactional
    public void deleteImage(Long id) {
        requireImage(id);
        imageTagMapper.deleteByGalleryImageIdPhysically(id);
        removeById(id);
        // The physical image file is intentionally kept for now to avoid accidental data loss.
        // A later cleanup job can safely remove orphaned files after backup or recycle-bin support exists.
    }

    private GalleryImage requireImage(Long id) {
        GalleryImage image = getById(id);
        if (image == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "Image not found");
        }
        return image;
    }

    private void requireGame(Long gameId) {
        Game game = gameMapper.selectById(gameId);
        if (game == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "Game not found");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "Image file is required");
        }
        String originalFilename = file.getOriginalFilename();
        String extension = getExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "Only jpg, jpeg, png, and webp are allowed");
        }
        String contentType = file.getContentType();
        if (StringUtils.hasText(contentType) && !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "Only jpg, jpeg, png, and webp are allowed");
        }
    }

    private String getExtension(String filename) {
        if (!StringUtils.hasText(filename) || !filename.contains(".")) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "Image file extension is required");
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }

    private String resolveMimeType(String contentType, String extension) {
        if (StringUtils.hasText(contentType)) {
            return contentType.toLowerCase(Locale.ROOT);
        }
        if ("png".equals(extension)) {
            return "image/png";
        }
        if ("webp".equals(extension)) {
            return "image/webp";
        }
        return "image/jpeg";
    }

    private ImageSize readImageSize(Path path) {
        try {
            BufferedImage image = ImageIO.read(path.toFile());
            if (image != null) {
                return new ImageSize(image.getWidth(), image.getHeight());
            }
        } catch (IOException ignored) {
            // Keep upload successful even if dimensions cannot be read by the current runtime.
        }
        return new ImageSize(0, 0);
    }

    private String normalizeType(String type, boolean required) {
        if (!StringUtils.hasText(type)) {
            if (required) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(),
                        "Image type must be character, photo, or screenshot");
            }
            return null;
        }
        String normalized = type.trim().toLowerCase(Locale.ROOT);
        if (!IMAGE_TYPES.contains(normalized)) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(),
                    "Image type must be character, photo, or screenshot");
        }
        return normalized;
    }

    private List<Long> parseTagIds(String tagIds) {
        if (!StringUtils.hasText(tagIds)) {
            return List.of();
        }
        List<Long> ids = new ArrayList<>();
        for (String item : tagIds.split(",")) {
            String value = item.trim();
            if (!value.isEmpty()) {
                try {
                    ids.add(Long.valueOf(value));
                } catch (NumberFormatException exception) {
                    throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "Tag IDs must be numbers");
                }
            }
        }
        return new ArrayList<>(new LinkedHashSet<>(ids));
    }

    private void ensureTagsExist(List<Long> tagIds) {
        if (tagIds.isEmpty()) {
            return;
        }
        Long count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>().in(Tag::getId, tagIds));
        if (count != tagIds.size()) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "Some tags do not exist");
        }
    }

    private List<Long> findImageIdsByAllTags(List<Long> tagIds) {
        if (tagIds.isEmpty()) {
            return List.of();
        }
        List<ImageTag> relations = imageTagMapper.selectList(new LambdaQueryWrapper<ImageTag>()
                .in(ImageTag::getTagId, tagIds));
        Map<Long, Set<Long>> tagIdsByImageId = relations.stream()
                .collect(Collectors.groupingBy(
                        ImageTag::getGalleryImageId,
                        Collectors.mapping(ImageTag::getTagId, Collectors.toSet())));
        return tagIdsByImageId.entrySet().stream()
                .filter(entry -> entry.getValue().containsAll(tagIds))
                .map(Map.Entry::getKey)
                .toList();
    }

    private void syncTags(Long imageId, List<Long> tagIds) {
        imageTagMapper.deleteByGalleryImageIdPhysically(imageId);
        for (Long tagId : tagIds) {
            ImageTag relation = new ImageTag();
            relation.setGalleryImageId(imageId);
            relation.setTagId(tagId);
            imageTagMapper.insert(relation);
        }
    }

    private List<TagVO> listTagsByImageId(Long imageId) {
        List<ImageTag> relations = imageTagMapper.selectList(new LambdaQueryWrapper<ImageTag>()
                .eq(ImageTag::getGalleryImageId, imageId));
        List<Long> tagIds = relations.stream()
                .map(ImageTag::getTagId)
                .distinct()
                .toList();
        if (tagIds.isEmpty()) {
            return List.of();
        }
        return tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                        .in(Tag::getId, tagIds)
                        .orderByAsc(Tag::getName))
                .stream()
                .map(this::toTagVO)
                .toList();
    }

    private Long normalizePage(Long page) {
        return page == null || page < 1 ? 1L : page;
    }

    private Long normalizeSize(Long size) {
        if (size == null || size < 1) {
            return 20L;
        }
        return Math.min(size, 100L);
    }

    private ImageCardVO toCardVO(GalleryImage image, List<TagVO> tags) {
        ImageCardVO vo = new ImageCardVO();
        vo.setId(image.getId());
        vo.setGameId(image.getGameId());
        vo.setType(image.getType());
        vo.setTitle(image.getTitle());
        vo.setDescription(image.getDescription());
        vo.setImageUrl(image.getImageUrl());
        vo.setThumbnailUrl(image.getThumbnailUrl());
        vo.setWidth(image.getWidth());
        vo.setHeight(image.getHeight());
        vo.setSortOrder(image.getSortOrder());
        vo.setTags(tags);
        vo.setCreatedAt(image.getCreatedAt());
        return vo;
    }

    private ImageVO toVO(GalleryImage image, List<TagVO> tags) {
        ImageVO vo = new ImageVO();
        vo.setId(image.getId());
        vo.setGameId(image.getGameId());
        vo.setType(image.getType());
        vo.setTitle(image.getTitle());
        vo.setDescription(image.getDescription());
        vo.setImageUrl(image.getImageUrl());
        vo.setThumbnailUrl(image.getThumbnailUrl());
        vo.setOriginalFilename(image.getOriginalFilename());
        vo.setFileSize(image.getFileSize());
        vo.setWidth(image.getWidth());
        vo.setHeight(image.getHeight());
        vo.setMimeType(image.getMimeType());
        vo.setSortOrder(image.getSortOrder());
        vo.setTags(tags);
        vo.setCreatedAt(image.getCreatedAt());
        vo.setUpdatedAt(image.getUpdatedAt());
        return vo;
    }

    private TagVO toTagVO(Tag tag) {
        TagVO vo = new TagVO();
        vo.setId(tag.getId());
        vo.setName(tag.getName());
        vo.setColor(tag.getColor());
        vo.setCreatedAt(tag.getCreatedAt());
        vo.setUpdatedAt(tag.getUpdatedAt());
        return vo;
    }

    private record ImageSize(Integer width, Integer height) {
    }
}
