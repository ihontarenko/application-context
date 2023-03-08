package pro.javadev.bean.processor;

import pro.javadev.bean.context.ApplicationContext;
import pro.javadev.bean.context.ApplicationContextAware;

public class ApplicationContextAwareBeanProcessor implements BeanProcessor {

    private final ApplicationContext context;

    public ApplicationContextAwareBeanProcessor(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void process(Object object) {
        if (object instanceof ApplicationContextAware bean) {
            bean.setApplicationContext(context);
        }
    }

}
