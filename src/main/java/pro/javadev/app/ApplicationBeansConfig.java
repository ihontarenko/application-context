package pro.javadev.app;

import pro.javadev.app.services.ServiceA;
import pro.javadev.bean.Bean;
import pro.javadev.bean.Configuration;

import java.util.Map;

@Configuration
public class ApplicationBeansConfig {

    @Bean
    public Map<String, String> envs(String name) {
        return Map.of("k", getClass().getName(), "n", name);
    }

    @Bean("anotherService")
    public ServiceA serviceA() {
        return new ServiceA();
    }

}
