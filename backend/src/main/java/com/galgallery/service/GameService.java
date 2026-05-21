package com.galgallery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.galgallery.common.PageResult;
import com.galgallery.dto.GameCreateDTO;
import com.galgallery.dto.GameQueryDTO;
import com.galgallery.dto.GameUpdateDTO;
import com.galgallery.entity.Game;
import com.galgallery.vo.GameCardVO;
import com.galgallery.vo.GameVO;

public interface GameService extends IService<Game> {

    PageResult<GameCardVO> pageGames(GameQueryDTO query);

    GameVO getGame(Long id);

    GameVO createGame(GameCreateDTO dto);

    GameVO updateGame(Long id, GameUpdateDTO dto);

    void deleteGame(Long id);
}
