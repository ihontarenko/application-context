package pro.javadev.bean.context;

import pro.javadev.bean.BeanFacrotyBuilder;
import pro.javadev.bean.BeanFactory;
import pro.javadev.bean.processor.BeanProcessor;
import pro.javadev.bean.processor.Processable;

public class AnnotationApplicationContext implements ApplicationContext, Processable {

    private final BeanFactory factory;

    private AnnotationApplicationContext(BeanFactory factory) {
        this.factory = factory;
        this.factory.setApplicationContext(this);
    }

    public static ApplicationContext run(Class<?>... classes) {
        Class<?>[]  root    = classes == null ? new Class<?>[]{ApplicationContext.class} : classes;
        BeanFactory factory = new BeanFacrotyBuilder(root).build();

        return new AnnotationApplicationContext(factory);
    }

    @Override
    public <T> T getBean(Class<T> beanType) {
        return factory.getBean(beanType);
    }

    @Override
    public <T> T getBean(String beanName) {
        return factory.getBean(beanName);
    }

    @Override
    public <T> T getBean(Class<T> beanType, String beanName) {
        return factory.getBean(beanType, beanName);
    }

    @Override
    public void addBeanProcessor(BeanProcessor processor) {
        factory.addBeanProcessor(processor);
    }

    @Override
    public BeanFactory getFactory() {
        return factory;
    }
}
