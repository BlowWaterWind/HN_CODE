package com.ai.ecs.ecm.mall.wap.common.i18n;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class InitMessageI18N implements InitializingBean
{
    private static final Logger LOGGER = LoggerFactory.getLogger(InitMessageI18N.class);
    
    private static final String I18N_MESSAGE = "META-INF/i18n/message";
    
    @Override
    public void afterPropertiesSet() throws Exception
    {
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
        ResourceBundleMessageSource bundleMessageSource = new ResourceBundleMessageSource();
        bundleMessageSource.setBasenames(I18N_MESSAGE);
        LOGGER.info("加载资源文件：" + I18N_MESSAGE);
        
        MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(bundleMessageSource);
        com.ai.ecs.ecm.mall.wap.common.i18n.ErrorMessage.setErrorMessageSourceAccessor(messageSourceAccessor);
    }
}