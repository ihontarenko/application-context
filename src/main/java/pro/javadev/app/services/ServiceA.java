package pro.javadev.app.services;

import pro.javadev.app.UserService2;
import pro.javadev.bean.Bean;
import pro.javadev.bean.Inject;

import java.util.StringJoiner;

@Bean
public class ServiceA {

    private String name;

    @Inject("DUMMY_USER_SERVICE")
    private UserService2 userService2;

    public ServiceA(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ServiceA.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("secondName='" + userService2 + "'")
                .toString();
    }
}
