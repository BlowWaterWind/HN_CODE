package com.ai.ecs.ecm.mall.wap.platform.utils;

import net.sf.cglib.beans.BeanCopier;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cong 2016/3/8
 * @since 1.0
 */
public class BeanCopierUtils
{
    public static final ConcurrentHashMap<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<>();

    public static void copyProperties(Object source, Object target)
    {
        String beanKey = generateKey(source.getClass(), target.getClass());
        BeanCopier copier = beanCopierMap.get(beanKey);
        if (copier == null)
        {
            copier = BeanCopier.create(source.getClass(), target.getClass(), false);
            beanCopierMap.putIfAbsent(beanKey, copier);
        }

        copier.copy(source, target, null);
    }

    private static String generateKey(Class<?> class1, Class<?> class2)
    {
        return class1.toString() + class2.toString();
    }
}
