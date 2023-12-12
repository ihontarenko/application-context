package pro.javadev.sandbox;

import pro.javadev.sandbox.services.ServiceInterface;
import pro.javadev.bean.Bean;
import pro.javadev.bean.BeanConstructor;

import java.util.StringJoiner;

@Bean
public class ServiceContainer {

    private ServiceInterface serviceA;

    @BeanConstructor
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
