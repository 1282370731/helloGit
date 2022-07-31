package com.test;

import java.util.ArrayList;
import java.util.List;

public class CarTest {
    public static void main(String[] args) {
        List<Car> cars= new ArrayList<>();
        Car c1=new Car("宝马",300000,"白色");
        Car c2=new Car("五菱宏光",30000,"红色");
        Car c3=new Car("奔驰",588888,"黑色");
        cars.add(c1);
        cars.add(c2);
        cars.add(c3);
        for (Car car:cars) {
            if(car.getPrice()>100000){
                System.out.println("价格高于100000的有：品牌："+car.getBrand()+"\t价格："+car.getPrice()+"\t颜色："+car.getColor());
            }
        }
    }


}
