package pro.javadev.app;

import pro.javadev.app.services.ServiceA;
import pro.javadev.app.services.ServiceB;
import pro.javadev.app.services.ServiceInterface;
import pro.javadev.bean.Bean;
import pro.javadev.bean.Configuration;
import pro.javadev.bean.Name;

@Configuration
public class ApplicationBeansConfig {

    @Bean
    public String userName() {
        return "Chuck Norris";
    }
//
//    @Bean("ServiceA1")
//    public ServiceInterface serviceA(String name) {
//        return new ServiceA(name);
//    }
//
//    @Bean("ServiceB1")
//    public ServiceInterface serviceB() {
//        return new ServiceB();
//    }

}
