package com.hnj.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hnj.common.CustomException;
import com.hnj.common.R;
import com.hnj.domain.Setmeal;
import com.hnj.domain.SetmealDish;
import com.hnj.dto.SetmealDto;
import com.hnj.service.SetmealService;
import com.hnj.mapper.SetmealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
    implements SetmealService{
    @Autowired
    private SetmealDishServiceImpl setmealDishService;

    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes=setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        Long count = this.count(queryWrapper);
        if(count>0){
            throw new CustomException("套餐正在售卖!");
        }
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }

    public R<String> updateStatus(Integer status, List<Long> ids){
        LambdaUpdateWrapper<Setmeal> wrapper=new LambdaUpdateWrapper<>();
        for (Long id:ids) {
            wrapper.clear();
            wrapper.eq(Setmeal::getId,id).set(Setmeal::getStatus,status);
            this.update(wrapper);
        }
        return R.success("success");
    }

}




