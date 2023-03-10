package pro.javadev.bean;

import pro.javadev.bean.context.ApplicationContextAware;
import pro.javadev.bean.definition.BeanDefinition;
import pro.javadev.bean.processor.Processable;

import java.lang.reflect.Method;
import java.util.List;

public interface BeanFactory extends Processable, ApplicationContextAware {

    <T> T getBean(Class<T> type);

    <T> T getBean(String beanName);

    String resolveBeanName(Class<?> type, String likelyName);

    List<String> getBeanNames(Class<?> type);

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
