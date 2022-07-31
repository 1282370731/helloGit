package com.hnj.service;

import com.hnj.domain.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hnj.dto.DishDto;

/**
 *
 */
public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
