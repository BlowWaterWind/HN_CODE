package com.ai.ecs.ecm.mall.wap.platform.annotation;

/**
 * ecs-ecm-project
 * <p>
 *跨站请求仿照注解
 * @author fengrh 2016/12/22  16:47
 * @see
 * @since 1.0
 * 修改人：fengrh 修改时间：2016/12/22  16:47 修改备注：
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyCSRFToken {
    /**
     * 需要验证防跨站请求
     *
     */
    public abstract boolean verify() default true;
}
