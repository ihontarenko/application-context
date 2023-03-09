package pro.javadev.bean.context;

import pro.javadev.bean.BeanFactory;
import pro.javadev.bean.processor.Processable;

public interface ApplicationContext extends Processable {

    <T> T getBean(Class<T> type);

    <T> T getBean(String name);

    BeanFactory getBeanFactory();

}
