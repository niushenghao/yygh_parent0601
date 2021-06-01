package com.lening.yygh.common.result;

import com.sun.javafx.geom.transform.GeneralTransform3D;
import org.junit.Test;

/**
 * @author 牛胜浩
 * @date 2021/4/26 16:40
 */
public class EnumTest {

    @Test
    public void test1(){
        Integer sex = 1;
        GenderEnum men = GenderEnum.MEN;
        String message = men.getMessage();
        System.out.println(message);
    }
}
