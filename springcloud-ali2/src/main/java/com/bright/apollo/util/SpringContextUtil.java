package com.bright.apollo.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil  implements ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public ApplicationContext getContext(){
        return this.context;
    }

    public Object getBeanByName(String name){
        return context.getBean(name);
    }

    public <T> T getBean(Class<T> clazz){
        return context.getBean(clazz);
    }
}
