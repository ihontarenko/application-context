package pro.javadev.bean.processor;

import pro.javadev.bean.context.ApplicationContext;

public interface BeanProcessor {

    void process(Object bean, ApplicationContext context);

}
