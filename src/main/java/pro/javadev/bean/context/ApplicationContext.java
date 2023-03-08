package pro.javadev.bean.context;

import pro.javadev.bean.BeanFactory;
import pro.javadev.bean.processor.BeanProcessor;

public interface ApplicationContext {

    <T> T getBean(Class<T> type);

    <T> T getBean(String name);

    void addBeanProcessor(BeanProcessor processor);

    BeanFactory getBeanFactory();

}
