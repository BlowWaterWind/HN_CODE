package com.ai.ecs.ecm.mall.wap.platform.utils;

import java.util.Random;

/**
 * <pre>
 * 包名称：com.ai.ecs.mall.platform.utils
 * 类描述：随机字符串
 * 创建人：R.H.Feng
 * 创建时间：2015/10/5 19:19
 * </pre>
 *
 * @version 1.0.0
 */
public class CharacterUtils {

    public static String getRandomString(int length){
        Random random = new Random();

        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < length; ++i){
            int number = random.nextInt(3);
            long result = 0;

            switch(number){
                case 0:
                    result = Math.round(Math.random() * 25 + 65);
                    sb.append(String.valueOf((char)result));
                    break;
                case 1:
                    result = Math.round(Math.random() * 25 + 97);
                    sb.append(String.valueOf((char)result));
                    break;
                case 2:
                    sb.append(String.valueOf(new Random().nextInt(10)));
                    break;
            }
        }
        return sb.toString();
    }
    public static void main(String[] args){
        System.out.println(getRandomString(6));
    }
}
