package com.ai.ecs.ecm.mall.wap.modules.goods.controller.simbank.common;

import com.ai.ecs.common.utils.JedisClusterUtils;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Created by cc on 2018/3/26.
 * spdb ccb用
 *
 */
@RequestMapping("common")
@Controller
public class CaptchaCommonController {

    protected Logger logger = LoggerFactory.getLogger(getClass());


    private static final char[] CHARS = {'2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private static final int FONT_SIZE = 40;


    private static Random random = new Random(); //随机数对象

    private static String getRandomString() {

        StringBuilder buffer = new StringBuilder();  //字符串缓存
        for (int i = 0; i < 4; i++)  //六次循环获取字符
        {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);  //每次随机取一个字符
        }
        return buffer.toString();
    }


    private static Color getRandomColor() {
        return new Color(222,222,222);
        //radial-gradient(ellipse at center,#fadce9 1%,#f6c3cb 55%,#f7cbd4 100%)
        //return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }


    /**
     * 字体颜色是背景颜色的反色
     * @param c
     * @return
     */
    private static Color getReverseColor(Color c) {
        //浦发套餐办理使用
        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
    }

    @RequestMapping("/getCaptchaImage.do")
    public void getCaptchaImage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("image/jpeg");  //设置输出类型    不可省略

        String randomString = getRandomString();  //调用生成随机字符串方法获取并接受随机字符串
        JedisClusterUtils.set(request.getSession().getId().toString() + "VERIFY_CODE", randomString, 5 * 60); //将字符串存储到Session中

        logger.info("浦发套餐确认验证码{}",randomString);

        int width = 200;
        int height = 60;

        Color color = getRandomColor(); //获取随机颜色   用于背景色
        Color reverse = getReverseColor(color);  //反色   用于前景色

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);       //创建一个彩色图片
        Graphics2D g = bi.createGraphics();  //获取绘图对象
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE));  //设置字体
        g.setColor(color);   //设置背景颜色
        g.fillRect(0, 0, width, height); //绘制背景
        g.setColor(reverse);  //设置字体颜色

        for (int i = 0; i < randomString.length(); i++) {
            String temp = randomString.substring(i, i + 1);
            g.drawString(temp, 30 * i + 40, 35 + random.nextInt(17));  //绘制随机字符，后面2个参数表示生成随机数的位置
        }


        for (int i = 0, n = random.nextInt(50); i < n; i++)  //画最多50个噪音点
        {
            g.drawRect(random.nextInt(width), random.nextInt(height), 1, 1); //随机噪音点
        }

        ServletOutputStream out = response.getOutputStream();   //获取输出流

        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  //编码器
        encoder.encode(bi);    //对图片进行编码
        out.flush();    //输出到客户端
    }

}
