package pro.javadev.app.services;

import pro.javadev.bean.Bean;
import pro.javadev.bean.Inject;

import java.util.StringJoiner;

@Bean
public class ServiceA {

    private String name;

    @Inject("NAME_OF_USER2")
    private String secondName;

    public ServiceA(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ServiceA.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("secondName='" + secondName + "'")
                .toString();
    }
}
