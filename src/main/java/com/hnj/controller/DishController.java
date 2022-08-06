package com.hnj.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hnj.common.R;
import com.hnj.domain.Category;
import com.hnj.domain.Dish;
import com.hnj.domain.DishFlavor;
import com.hnj.dto.DishDto;
import com.hnj.service.impl.CategoryServiceImpl;
import com.hnj.service.impl.DishFlavorServiceImpl;
import com.hnj.service.impl.DishServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("dish")
public class DishController {
    @Autowired
    private DishServiceImpl dishService;
    @Autowired
    private CategoryServiceImpl categoryService;
    @Autowired
    private DishFlavorServiceImpl dishFlavorService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page<Dish> pageInfo=new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list=records.stream().map((item)->{
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId=item.getCategoryId();
            Category category=categoryService.getById(categoryId);
            if(category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        DishDto dishDto=dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        String key="dish"+dishDto.getCategoryId();
        dishService.saveWithFlavor(dishDto);
        stringRedisTemplate.delete(key);
        return R.success("新增菜品成功！");
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        String key="dish"+dishDto.getCategoryId();
        dishService.updateWithFlavor(dishDto);
        stringRedisTemplate.delete(key);
        return R.success("修改菜品成功！");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status,@RequestParam List<Long> ids){
        return dishService.updateStatus(status,ids);
    }

    @DeleteMapping
    public R deleteById(@RequestParam List<Long> ids){
        return dishService.deleteById(ids);
    }


    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> dishDtos = null;
        String key="dish:"+dish.getCategoryId();
        String dishDtoStr = stringRedisTemplate.opsForValue().get(key);
        dishDtos= JSON.parseArray(dishDtoStr,DishDto.class);
        if(dishDtos!=null){
            return R.success(dishDtos);
        }

        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishes=dishService.list(queryWrapper);
        dishDtos=dishes.stream().map((item)->{
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId=item.getCategoryId();
            Category category=categoryService.getById(categoryId);
            if(category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long id = item.getId();
            dishDto.setFlavors(dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId,id)));
            return dishDto;
        }).collect(Collectors.toList());
        String dishDtoListStr=JSON.toJSONString(dishDtos);
        stringRedisTemplate.opsForValue().set(key,dishDtoListStr,60, TimeUnit.MINUTES);
        return R.success(dishDtos);
    }
}
