package com.galgallery.controller;

import com.galgallery.common.PageResult;
import com.galgallery.common.Result;
import com.galgallery.dto.GameCreateDTO;
import com.galgallery.dto.GameQueryDTO;
import com.galgallery.dto.GameUpdateDTO;
import com.galgallery.service.GameService;
import com.galgallery.vo.GameCardVO;
import com.galgallery.vo.GameVO;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * Game management APIs for listing, reading, creating, updating, and deleting games.
 */
@Validated
@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Page games with optional keyword and image type filters.
     */
    @GetMapping
    public Result<PageResult<GameCardVO>> pageGames(@Valid @ModelAttribute GameQueryDTO query) {
        return Result.success(gameService.pageGames(query));
    }

    /**
     * Get one game by ID.
     */
    @GetMapping("/{id}")
    public Result<GameVO> getGame(@PathVariable Long id) {
        return Result.success(gameService.getGame(id));
    }

    /**
     * Create a new game.
     */
    @PostMapping
    public Result<GameVO> createGame(@Valid @RequestBody GameCreateDTO dto) {
        return Result.success(gameService.createGame(dto));
    }

    /**
     * Update an existing game.
     */
    @PutMapping("/{id}")
    public Result<GameVO> updateGame(@PathVariable Long id, @Valid @RequestBody GameUpdateDTO dto) {
        return Result.success(gameService.updateGame(id, dto));
    }

    /**
     * Logically delete an existing game.
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return Result.success();
    }
}
