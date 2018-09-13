package com.ai.ecs.ecm.mall.wap.common.i18n;

import com.ai.ecs.common.utils.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.Assert;

import java.util.Locale;

public class ErrorMessage
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorMessage.class);
    
    private static final String ERROR_CODE_PREFIX = "ERROR_";
    
    static
    {
        SpringContextHolder.getBean(com.ai.ecs.ecm.mall.wap.common.i18n.InitMessageI18N.class);
    }
    
    // 错误信息的国际化信息
    private static MessageSourceAccessor errorMessageSourceAccessor;
    
    public static void setErrorMessageSourceAccessor(MessageSourceAccessor errorMessageSourceAccessor)
    {
        ErrorMessage.errorMessageSourceAccessor = errorMessageSourceAccessor;
    }
    
    public static String getError(String code)
    {
        
        String errorMessage = getErrorMessage(ERROR_CODE_PREFIX + code);
        
        return errorMessage;
    }
    
    private static String getErrorMessage(String code)
    {
        try
        {
            Assert.notNull(errorMessageSourceAccessor, "请先设置错误消息的国际化资源");
            return errorMessageSourceAccessor.getMessage(code, new Object[] {}, Locale.SIMPLIFIED_CHINESE);
        }
        catch (NoSuchMessageException e)
        {
            LOGGER.error("不存在对应的错误键：{}，请检查是否在i18n/message的错误资源", code);
            throw e;
        }
    }
}
