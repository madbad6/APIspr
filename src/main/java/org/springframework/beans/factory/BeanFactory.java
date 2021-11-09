package org.springframework.beans.factory;

import org.springframework.beans.factory.sterotupe.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BeanFactory {
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
                }
            }
        }
    }

    private Map<String, Object> singleton = new HashMap<>();

    public Object getBean(String beanName){
        return singleton.get(beanName);
    }
}
