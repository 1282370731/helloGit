package com.test;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Test1 {
    public static void main(String[] args) {
        int[] arr=randomArray();
        ArrayList arrayList=new ArrayList();
        int sum=0;
        int index=0;
        //System.out.println(Arrays.toString(arr));
        for (int i = 0; i < arr.length; i++) {
            sum+=arr[i];
            if(i%2==0&&arr[i]%2==0){
                arrayList.add(arr[i]);
                index++;
            }
        }
        System.out.println("随机数组总和为："+sum);
        System.out.println("角标和对应的元素都为偶数："+ arrayList);
    }

    public static int[] randomArray(){
        int[] arr=new int[5];
        for (int i = 0; i < arr.length; i++) {
            int num=(int) (Math.random()*100);
            arr[i]=num;
        }
        return arr;
    }
}
