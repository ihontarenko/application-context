package pro.javadev.app.services;

import pro.javadev.bean.Bean;
import pro.javadev.bean.context.ApplicationContext;
import pro.javadev.bean.context.ApplicationContextAware;

@Bean
public class ServiceB implements ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return context;
    }
}
