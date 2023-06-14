package pro.javadev.sandbox.services;

import pro.javadev.bean.Bean;
import pro.javadev.bean.Scope;
import pro.javadev.bean.context.ApplicationContext;
import pro.javadev.bean.context.ApplicationContextAware;

@Bean(scope = Scope.PROTOTYPE)
public class ServiceB implements ApplicationContextAware, ServiceInterface {

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
