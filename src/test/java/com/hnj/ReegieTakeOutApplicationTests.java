package com.hnj;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
class ReegieTakeOutApplicationTests {

    @Test
    void contextLoads() {
        List<String> list= new ArrayList<>();
        Collections.addAll(list,"张卫健","张无忌","张三丰","张三","李四","赵六");

        list.stream().limit(3).forEach(s -> System.out.println(s));

    }

}
