package pro.javadev.sandbox;

import pro.javadev.sandbox.services.ServiceA;
import pro.javadev.sandbox.services.ServiceB;
import pro.javadev.sandbox.services.ServiceInterface;
import pro.javadev.bean.Bean;
import pro.javadev.bean.BeanConfiguration;

@BeanConfiguration
public class ApplicationBeansConfig {

    @Bean
    public String userName() {
        return "Chuck Norris";
    }

    @Bean("ServiceA1")
    public ServiceInterface serviceA(String name) {
        return new ServiceA(name);
    }

    @Bean("ServiceB1")
    public ServiceInterface serviceB() {
        return new ServiceB();
    }

}
