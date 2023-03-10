package pro.javadev.app;

import pro.javadev.app.services.ServiceInterface;
import pro.javadev.bean.Bean;
import pro.javadev.bean.BeanContructor;

import java.util.StringJoiner;

@Bean
public class ServiceContainer {

    private ServiceInterface serviceA;

    @BeanContructor
    public ServiceContainer(ServiceInterface serviceA) {
        this.serviceA = serviceA;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ServiceContainer.class.getSimpleName() + "[", "]")
                .add("serviceA=" + serviceA)
                .toString();
    }
}
