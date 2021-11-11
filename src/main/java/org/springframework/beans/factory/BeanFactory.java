package org.springframework.beans.factory;

import org.springframework.beans.factory.annatation.Autowired;
import org.springframework.beans.factory.sterotupe.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BeanFactory {

    private Map<String, Object> singleton = new HashMap<>();

    public Object getBean(String beanName){
        return singleton.get(beanName);
    }

    public void instantiate(String basePackage) {

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        String patch = basePackage.replace('.', '/');
        Enumeration<URL> resources = null;
        try {
            resources = classLoader.getResources(patch);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (resources.hasMoreElements()){
            URL resource = resources.nextElement();

            File file = null;
            try {
                file = new File(resource.toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            for (File classFile : file.listFiles()){
                String fileName = classFile.getName();

                if(fileName.endsWith(".class")){
                    String className = fileName.substring(0, fileName.lastIndexOf("."));
                    Class classObject = null;
                    try {
                        classObject = Class.forName(basePackage + "." + className);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(classObject.isAnnotationPresent(Component.class)){
                        System.out.println("Component: " + classObject);
                    }
                    Object instance = null;
                    try {
                        instance = classObject.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    String beanName = className.substring(0,1).toLowerCase() + className.substring(1);
                    singleton.put(beanName, instance);
                }
            }
        }
    }

    public void populateProperties(){
        System.out.println("==populateProperties==");
        for (Object object : singleton.values()){
            for (Field field : object.getClass().getDeclaredFields()){
                if(field.isAnnotationPresent(Autowired.class)){
                    for (Object dependency : singleton.values()){
                        if(dependency.getClass().equals(field.getType())){
                            String setterName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                            System.out.println("Setter name = " + setterName);
                            Method setter = null;
                            try {
                                setter = object.getClass().getMethod(setterName, dependency.getClass());
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                            try {
                                setter.invoke(object, dependency);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public void injectBeanName(){
        for (String name : singleton.keySet()){
            Object bean = singleton.get(name);
            if(bean instanceof BeanNameAware){
                ((BeanNameAware) bean).setBeanName(name);
            }
        }
    }

    public void initializeBeans() {
        for(Object bean : singleton.values()){
            if (bean instanceof InitializingBean){
                ((InitializingBean) bean).afterPropertiesSet();
            }
        }
    }
}
