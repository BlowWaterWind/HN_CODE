package com.ai.ecs.ecm.mall.wap.platform.utils;


import com.ai.ecs.common.utils.SpringContextHolder;
import com.ai.ecs.ecop.sys.service.DictService;

/**
 * 字典工具类
 * simple introduction
 *
 *EhCache 重启会刷缓存
 * <p>detailed comment
 * @author MaYunLong 2016-3-28
 * @see
 * @since 1.0
 */
public class DictUtil
{
    
    private static DictService dictService = SpringContextHolder.getBean(DictService.class);
    
    public static String getDictValue(String label, String type, String defaultLabel){
        return dictService.getDictValue(label,type,defaultLabel);
    }
    
    public static String getDictLabel(String label, String type, String defaultLabel){
        return dictService.getDictLabel(label,type,defaultLabel);
    }
}
