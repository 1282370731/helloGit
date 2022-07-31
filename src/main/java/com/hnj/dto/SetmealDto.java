package com.hnj.dto;


import com.hnj.domain.Setmeal;
import com.hnj.domain.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
