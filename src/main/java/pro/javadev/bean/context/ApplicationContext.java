package pro.javadev.bean.context;

import pro.javadev.bean.BeanFactory;
import pro.javadev.bean.processor.Processable;

public interface ApplicationContext extends Processable {

    <T> T getBean(Class<T> beanType, String beanName);

    <T> T getBean(Class<T> beanType);

    <T> T getBean(String beanName);

    BeanFactory getFactory();

}
