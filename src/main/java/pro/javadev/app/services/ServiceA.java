package pro.javadev.app.services;

import pro.javadev.bean.Bean;

import java.util.StringJoiner;

@Bean
public class ServiceA {

    private String name;


    public ServiceA(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ServiceA.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .toString();
    }
}
