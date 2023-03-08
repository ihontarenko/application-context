package pro.javadev.app.services;

import pro.javadev.bean.Bean;
import pro.javadev.bean.Inject;

import java.util.Map;

@Bean
public class ServiceHandler {

    private ServiceA            serviceA;
    private ServiceB            serviceB;
    private Map<String, String> envs;

    @Inject
    public ServiceHandler(ServiceA serviceA, ServiceB serviceB, Map<String, String> envs) {
        this.serviceA = serviceA;
        this.serviceB = serviceB;
        this.envs = envs;
    }

}
