package pro.javadev.bean.context;

import pro.javadev.bean.AnnotationBeanFactory;
import pro.javadev.bean.BeanFactory;
import pro.javadev.bean.processor.BeanProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationApplicationContext implements ApplicationContext {

    private final Map<String, Object> beans      = new ConcurrentHashMap<>();
    private final BeanFactory           factory;
    private final List<BeanProcessor>   processors = new ArrayList<>();

    private AnnotationApplicationContext(BeanFactory factory) {
        this.factory = factory;
    }

    public static ApplicationContext run(Class<?>... classes) {
        Class<?>[]  root    = classes == null ? new Class<?>[]{ApplicationContext.class} : classes;
        BeanFactory factory = new AnnotationBeanFactory();

        factory.scan(root);

        return new AnnotationApplicationContext(factory);
    }

    @SuppressWarnings({"all"})
    @Override
    public <T> T getBean(Class<T> type) {
        return getBean(factory.createBeanName(type));
    }

    @SuppressWarnings({"all"})
    @Override
    public <T> T getBean(String beanName) {
        return (T) beans.computeIfAbsent(beanName, name -> {
            Object bean = factory.getBean(name);

            if (bean != null) {
                processors.forEach(processor -> processor.process(bean));
            }

            return bean;
        });
    }

    @Override
    public void addBeanProcessor(BeanProcessor processor) {
        processors.add(processor);
    }

    @Override
    public BeanFactory getBeanFactory() {
        return factory;
    }
}
