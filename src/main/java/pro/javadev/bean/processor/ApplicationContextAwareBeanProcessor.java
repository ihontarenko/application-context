package pro.javadev.bean.processor;

import pro.javadev.bean.context.ApplicationContext;
import pro.javadev.bean.context.ApplicationContextAware;

public class ApplicationContextAwareBeanProcessor implements BeanProcessor {

    @Override
    public void process(Object object, ApplicationContext context) {
        if (object instanceof ApplicationContextAware bean) {
            bean.setApplicationContext(context);
        }
    }

}
