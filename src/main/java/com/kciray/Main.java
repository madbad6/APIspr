package com.kciray;

import org.springframework.beans.factory.BeanFactory;

public class Main {
    public static void main(String[] args){
        BeanFactory beanFactory = new BeanFactory();
        beanFactory.instantiate("com.kciray");
    }
}
