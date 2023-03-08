package pro.javadev.app;

import pro.javadev.bean.Bean;

public class BeansContainer {

    @Bean("nameOfUser")
    public String testString() {
        return getClass().getName();
    }

}
