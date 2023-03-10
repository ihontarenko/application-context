package pro.javadev.bean;

import pro.javadev.bean.context.ApplicationContextAware;
import pro.javadev.bean.definition.BeanDefinition;
import pro.javadev.bean.processor.Processable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public interface BeanFactory extends Processable, ApplicationContextAware {

    default String getBeanName(AnnotatedElement element, Class<? extends Annotation> annotationType) {
        String value = null;

        if (element.isAnnotationPresent(annotationType)) {
            try {
                Annotation annotation       = element.getAnnotation(annotationType);
                Method     annotationMethod = annotationType.getMethod("value");
                value = (String) annotationMethod.invoke(annotation);
                value = value.isBlank() ? null : value;
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    <T> T getBean(Class<T> beanType);

    <T> T getBean(Class<T> beanType, String beanName);

    <T> T getBean(String beanName);

    String resolveBeanName(Class<?> type, String likelyName);

    List<String> getBeanNames(Class<?> type);

    <T> T createBean(BeanDefinition definition);

    BeanDefinition getBeanDefinition(Class<?> type);

    BeanDefinition getBeanDefinition(String beanName);

    BeanDefinition createBeanDefinition(Class<?> interfaceType, List<Class<?>> subClasses);

    BeanDefinition createBeanDefinition(Class<?> type);

    BeanDefinition createBeanDefinition(Class<?> type, Class<?> factoryClass, Method factoryMethod);

    void registerBeanDefinition(BeanDefinition definition);

    String getBeanName(Class<?> type);

    String getBeanName(Method method);

    void scan(Class<?>... classes);

}
