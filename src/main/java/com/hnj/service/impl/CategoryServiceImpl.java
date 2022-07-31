package com.hnj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hnj.common.CustomException;
import com.hnj.domain.Category;
import com.hnj.domain.Dish;
import com.hnj.domain.Setmeal;
import com.hnj.service.CategoryService;
import com.hnj.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{
    @Autowired
    private DishServiceImpl dishService;

    @Autowired
    private SetmealServiceImpl setmealService;

    @Override
    public void remove(Long id){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        Long count= dishService.count(dishLambdaQueryWrapper);
        if(count>0){
            throw new CustomException("当前分类下存在菜品，禁止删除！");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        Long count2=setmealService.count(setmealLambdaQueryWrapper);
        if(count2>0){
            throw new CustomException("当前套餐下存在菜品，禁止删除！");
        }
        super.removeById(id);
    }

}




