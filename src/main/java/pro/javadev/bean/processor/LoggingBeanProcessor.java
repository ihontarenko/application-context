package pro.javadev.bean.processor;

import pro.javadev.bean.context.ApplicationContext;

import java.util.function.Consumer;

public class LoggingBeanProcessor implements BeanProcessor {

    private final Consumer<String> logger;

    public LoggingBeanProcessor(Consumer<String> logger) {
        this.logger = logger;
    }

    @Override
    public void process(Object bean, ApplicationContext context) {
        logger.accept(
                "-- BEAN CREATED: [" + bean.getClass().getName() + '@' + Integer.toHexString(bean.hashCode()) + "] "
        );
    }

}
