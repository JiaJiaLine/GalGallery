package com.galgallery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.galgallery.common.PageResult;
import com.galgallery.common.ResultCode;
import com.galgallery.dto.GameCreateDTO;
import com.galgallery.dto.GameQueryDTO;
import com.galgallery.dto.GameUpdateDTO;
import com.galgallery.entity.GalleryImage;
import com.galgallery.entity.Game;
import com.galgallery.exception.BusinessException;
import com.galgallery.mapper.GalleryImageMapper;
import com.galgallery.mapper.GameMapper;
import com.galgallery.service.GameService;
import com.galgallery.vo.GameCardVO;
import com.galgallery.vo.GameVO;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class GameServiceImpl extends ServiceImpl<GameMapper, Game> implements GameService {

    private static final Set<String> IMAGE_TYPES = Set.of("character", "photo", "screenshot");

    private final GalleryImageMapper galleryImageMapper;

    public GameServiceImpl(GalleryImageMapper galleryImageMapper) {
        this.galleryImageMapper = galleryImageMapper;
    }

    @Override
    public PageResult<GameCardVO> pageGames(GameQueryDTO query) {
        String type = normalizeType(query.getType());
        Page<Game> page = new Page<>(normalizePage(query.getPage()), normalizeSize(query.getSize()));
        LambdaQueryWrapper<Game> wrapper = new LambdaQueryWrapper<Game>()
                .like(StringUtils.hasText(query.getKeyword()), Game::getName, query.getKeyword())
                .orderByAsc(Game::getSortOrder)
                .orderByDesc(Game::getCreatedAt)
                .orderByDesc(Game::getId);

        Page<Game> result = page(page, wrapper);
        List<GameCardVO> records = result.getRecords().stream()
                .map(game -> toCardVO(game, countImages(game.getId(), type)))
                .toList();
        return new PageResult<>(records, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public GameVO getGame(Long id) {
        Game game = requireGame(id);
        return toVO(game, countImages(game.getId(), null));
    }

    @Override
    @Transactional
    public GameVO createGame(GameCreateDTO dto) {
        Game game = new Game();
        fillGame(game, dto.getName(), dto.getOriginalName(), dto.getCoverUrl(), dto.getDescription(),
                dto.getDeveloper(), dto.getReleaseDate(), dto.getSortOrder());
        save(game);
        return getGame(game.getId());
    }

    @Override
    @Transactional
    public GameVO updateGame(Long id, GameUpdateDTO dto) {
        Game game = requireGame(id);
        fillGame(game, dto.getName(), dto.getOriginalName(), dto.getCoverUrl(), dto.getDescription(),
                dto.getDeveloper(), dto.getReleaseDate(), dto.getSortOrder());
        updateById(game);
        return getGame(id);
    }

    @Override
    @Transactional
    public void deleteGame(Long id) {
        requireGame(id);
        removeById(id);
    }

    private Game requireGame(Long id) {
        Game game = getById(id);
        if (game == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "Game not found");
        }
        return game;
    }

    private void fillGame(Game game, String name, String originalName, String coverUrl, String description,
                          String developer, java.time.LocalDate releaseDate, Integer sortOrder) {
        game.setName(name);
        game.setOriginalName(originalName);
        game.setCoverUrl(coverUrl);
        game.setDescription(description);
        game.setDeveloper(developer);
        game.setReleaseDate(releaseDate);
        game.setSortOrder(sortOrder == null ? 0 : sortOrder);
    }

    private Long countImages(Long gameId, String type) {
        LambdaQueryWrapper<GalleryImage> wrapper = new LambdaQueryWrapper<GalleryImage>()
                .eq(GalleryImage::getGameId, gameId)
                .eq(StringUtils.hasText(type), GalleryImage::getType, type);
        return galleryImageMapper.selectCount(wrapper);
    }

    private String normalizeType(String type) {
        if (!StringUtils.hasText(type)) {
            return null;
        }
        String normalized = type.trim().toLowerCase();
        if (!IMAGE_TYPES.contains(normalized)) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(),
                    "Image type must be character, photo, or screenshot");
        }
        return normalized;
    }

    private Long normalizePage(Long page) {
        return page == null || page < 1 ? 1L : page;
    }

    private Long normalizeSize(Long size) {
        if (size == null || size < 1) {
            return 12L;
        }
        return Math.min(size, 100L);
    }

    private GameCardVO toCardVO(Game game, Long imageCount) {
        GameCardVO vo = new GameCardVO();
        vo.setId(game.getId());
        vo.setName(game.getName());
        vo.setOriginalName(game.getOriginalName());
        vo.setCoverUrl(game.getCoverUrl());
        vo.setDescription(game.getDescription());
        vo.setImageCount(imageCount);
        return vo;
    }

    private GameVO toVO(Game game, Long imageCount) {
        GameVO vo = new GameVO();
        vo.setId(game.getId());
        vo.setName(game.getName());
        vo.setOriginalName(game.getOriginalName());
        vo.setCoverUrl(game.getCoverUrl());
        vo.setDescription(game.getDescription());
        vo.setDeveloper(game.getDeveloper());
        vo.setReleaseDate(game.getReleaseDate());
        vo.setSortOrder(game.getSortOrder());
        vo.setImageCount(imageCount);
        vo.setCreatedAt(game.getCreatedAt());
        vo.setUpdatedAt(game.getUpdatedAt());
        return vo;
    }
}
