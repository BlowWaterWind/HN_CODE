package com.ai.ecs.ecm.mall.wap.platform.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ecs-ecm-project
 * <p>
 * 跨站请求仿照注解 刷新CSRFToken
 * @author fengrh 2016/12/22  16:46
 * @see
 * @since 1.0
 * 修改人：fengrh 修改时间：2016/12/22  16:46 修改备注：
 */
@Target({ java.lang.annotation.ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RefreshCSRFToken {
    /**
     * 刷新token
     *
     * @return
     */
    public abstract boolean refresh() default true;
}
