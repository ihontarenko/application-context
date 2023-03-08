package pro.javadev.bean;

import pro.javadev.bean.definition.BeanDefinition;

import java.lang.reflect.Method;

public interface BeanFactory {

    <T> T getBean(Class<T> type);

    <T> T getBean(String beanName);

    <T> T createBean(BeanDefinition definition);

    BeanDefinition getBeanDefinition(Class<?> type);

    BeanDefinition getBeanDefinition(String beanName);

    BeanDefinition createBeanDefinition(Class<?> type);

    BeanDefinition createBeanDefinition(Class<?> type, Class<?> factoryClass, Method factoryMethod);

    void registerBeanDefinition(BeanDefinition definition);

    String createBeanName(Class<?> type);

    String createBeanName(Method method);

    void scan(Class<?>... classes);

}
