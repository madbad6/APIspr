package com.kciray;

import org.springframework.beans.factory.BeanFactory;

public class Main {
    public static void main(String[] args){
        BeanFactory beanFactory = new BeanFactory();
        beanFactory.instantiate("com.kciray");
        beanFactory.populateProperties();
        beanFactory.injectBeanName();
        ProductService productService = (ProductService) beanFactory.getBean("productService");
        productService.getPromotionsService();
        PromotionsService promotionsService = (PromotionsService) beanFactory.getBean("promotionsService");

        System.out.println("Bean name = " + promotionsService.getBeanName());

        beanFactory.initializeBeans();
    }
}
