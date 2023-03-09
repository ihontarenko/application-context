package pro.javadev.app;

import pro.javadev.bean.Bean;

public class BeansContainer {

    @Bean("nameOfUser")
    public String testString() {
        return "John Doe";
    }

    @Bean("nameOfUser2")
    public String testString2() {
        return "Chuck Norris";
    }

}
