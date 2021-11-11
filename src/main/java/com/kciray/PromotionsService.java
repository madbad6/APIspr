package com.kciray;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.sterotupe.Component;

@Component
public class PromotionsService implements BeanNameAware {
    private String beanName;

    @Override
    public void setBeanName(String name) {
        beanName = name;
    }

    public String getBeanName(){
        return beanName;
    }
}
