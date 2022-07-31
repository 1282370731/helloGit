package com.hnj.service;

import com.hnj.domain.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hnj.dto.SetmealDto;

import java.util.List;

/**
 *
 */
public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);
}
