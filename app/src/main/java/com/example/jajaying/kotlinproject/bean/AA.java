package com.example.jajaying.kotlinproject.bean;


import java.util.Arrays;

/**
 * Created by wangyao3 on 2018/9/5.
 */

public class AA {


    public static void main(String[] s) {
         int[] array = {23, 11, 7, 29, 33, 59, 8, 20, 9, 3, 2, 6, 10, 44, 83, 28, 5, 1, 0, 36};
        shellSort(array);
    }

    private static void shellSort(int []array ) {
        int m = array.length;
        while (true) {
            // 本次增量的变化方式为 m/2
            m = m / 2;
            // 分组后的数组下标为n/m的摩
            for (int i = 0; i < m; i++) {
                // 分组后数组的数据为原数组下标摩为i的数
                for (int j = i + m; j < array.length; j += m) {
                    // 每组内部进行插入排序（此处使用直接插入排序方式，也可使用二分法插入）
                    int temp = array[j];
                    int k;
                    // 在前面已经遍历过的数字中比较若小于则往后移
                    for (k = j - m; k >= i; k -= m) {
                        if (temp < array[k]) {
                            array[k + m] = array[k];
                        } else {
                            break;
                        }
                    }
                    array[k + m] = temp;
                }
            }
            if (m == 1) {
                System.out.print("//array-----------------------==="+ Arrays.toString(array));

                break;
            }
        }
    }

}
