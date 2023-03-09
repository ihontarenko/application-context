package pro.javadev.app;

import pro.javadev.app.services.ServiceA;
import pro.javadev.bean.Bean;
import pro.javadev.bean.Configuration;
import pro.javadev.bean.Name;

import java.util.Map;

@Configuration
public class ApplicationBeansConfig {

    @Bean
    public Map<String, String> envs(@Name("NAME_OF_USER2") String name) {
        return Map.of("k", getClass().getName(), "n", name);
    }

    @Bean("anotherService")
    public ServiceA serviceA(@Name("NAME_OF_USER2") String name) {
        return new ServiceA(name);
    }

    @Bean("anotherServiceB")
    public ServiceA serviceAB(@Name("NAME_OF_USER") String name) {
        return new ServiceA(name);
    }

}
