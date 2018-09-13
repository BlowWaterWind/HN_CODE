package com.ai.ecs.ecm.mall.wap.platform.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * ecs-ecm-project
 * <p>
 * Token生成
 * @author fengrh 2016/12/22  16:44
 * @see
 * @since 1.0
 * 修改人：fengrh 修改时间：2016/12/22  16:44 修改备注：
 */
public class CSRFTokenUtil {
    public static String generate(HttpServletRequest request) {

        return UUID.randomUUID().toString();
    }
}
