package com.testhuamou.vltava.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author Rob
 * @date Create in 2:35 PM 2019/7/12
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext context = null;

    private static final String DEV = "dev";

    /* (non Javadoc)
     * @Title: setApplicationContext
     * @Description: spring获取bean工具类
     * @param applicationContext
     * @throws BeansException
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
    }

    // 传入线程中
    public <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    public <T> T getBean(Class<T> beanClazz) {
        return (T) context.getBean(beanClazz);
    }

    // 国际化使用
    public static String getMessage(String key) {
        return context.getMessage(key, null, Locale.getDefault());
    }

    /// 获取当前环境
    public static String getActiveProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }

    public boolean inDev(){
        return DEV.equals(getActiveProfile());
    }
}

