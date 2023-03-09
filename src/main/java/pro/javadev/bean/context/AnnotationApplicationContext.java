package pro.javadev.bean.context;

import pro.javadev.bean.AnnotationBeanFactory;
import pro.javadev.bean.BeanFactory;
import pro.javadev.bean.processor.BeanProcessor;
import pro.javadev.bean.processor.Processable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationApplicationContext implements ApplicationContext, Processable {

    private final Map<String, Object> beans = new ConcurrentHashMap<>();
    private final BeanFactory         factory;

    private AnnotationApplicationContext(BeanFactory factory) {
        this.factory = factory;
        this.factory.setApplicationContext(this);
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
            return factory.getBean(name);
        });
    }

    @Override
    public void addBeanProcessor(BeanProcessor processor) {
        factory.addBeanProcessor(processor);
    }

    @Override
    public BeanFactory getBeanFactory() {
        return factory;
    }
}
