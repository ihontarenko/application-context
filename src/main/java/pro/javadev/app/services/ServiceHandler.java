package pro.javadev.app.services;

import pro.javadev.bean.Bean;
import pro.javadev.bean.Inject;
import pro.javadev.bean.Name;

import java.util.Map;
import java.util.StringJoiner;

@Bean
public class ServiceHandler {

    private ServiceA            serviceA;
    private ServiceB            serviceB;
    private Map<String, String> envs;

    @Inject
    public ServiceHandler(@Name("ANOTHER_SERVICE_B") ServiceA serviceA, @Name("SERVICE_B") ServiceB serviceB, Map<String, String> envs) {
        this.serviceA = serviceA;
        this.serviceB = serviceB;
        this.envs = envs;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ServiceHandler.class.getSimpleName() + "[", "]")
                .add("serviceA=" + serviceA)
                .add("serviceB=" + serviceB)
                .add("envs=" + envs)
                .toString();
    }
}
