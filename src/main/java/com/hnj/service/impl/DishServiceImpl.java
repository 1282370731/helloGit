package com.hnj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hnj.common.R;
import com.hnj.domain.Dish;
import com.hnj.domain.DishFlavor;
import com.hnj.domain.Setmeal;
import com.hnj.domain.SetmealDish;
import com.hnj.dto.DishDto;
import com.hnj.service.DishService;
import com.hnj.mapper.DishMapper;
import com.hnj.service.SetmealDishService;
import com.hnj.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService{

    @Autowired
    private DishFlavorServiceImpl dishFlavorService;
    @Autowired
    private SetmealDishServiceImpl setmealDishService;
    @Autowired
    private SetmealServiceImpl setmealService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish=this.getById(id);
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> dishFlavors=dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(dishFlavors);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.updateBatchById(flavors);
    }

    public R deleteById(List<Long> id){
        LambdaQueryWrapper<SetmealDish> wrapper=new LambdaQueryWrapper<>();
        for (Long ids:id) {
            wrapper.clear();
            wrapper.eq(SetmealDish::getDishId,ids);
            Long count=setmealDishService.count(wrapper);
            Dish dish=this.getById(ids);
            if(count>0){
                return R.error(dish.getName()+"菜品在套餐中，不可删除！");
            }
            if(dish.getStatus()==1){
                return R.error(dish.getName()+"菜品在售卖中，不可删除！");
            }
        }
        this.removeByIds(id);
        return R.success("删除菜品成功！");
    }

    public R<String> updateStatus(Integer status,List<Long> ids){
        LambdaUpdateWrapper<Dish> wrapper=new LambdaUpdateWrapper<>();
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        for (Long id:ids) {
            if(status==0){
                queryWrapper.clear();
                queryWrapper.eq(SetmealDish::getDishId,id);
                List<SetmealDish> setmealDish=setmealDishService.list(queryWrapper);
                if(setmealDish.size()>0){
                    for (SetmealDish setmealDish1:setmealDish) {
                        Setmeal setmeal=setmealService.getById(setmealDish1.getSetmealId());
                        if(setmeal.getStatus()==1){
                            return R.error("套餐在售卖中，不可停售！");
                        }
                    }
                }
            }
            wrapper.clear();
            wrapper.eq(Dish::getId,id).set(Dish::getStatus,status);
            this.update(wrapper);
        }
        return R.success("success");
    }
}




