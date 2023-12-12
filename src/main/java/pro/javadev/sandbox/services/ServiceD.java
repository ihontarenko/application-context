package pro.javadev.sandbox.services;

import pro.javadev.bean.BeanConstructor;
import pro.javadev.bean.BeanInjection;

import java.util.StringJoiner;

public class ServiceD implements ServiceInterface {

    private String name;

    @BeanInjection("REMOTE_USER_SERVICE")
    private UserService userService;

//    @BeanInjection
    private Storage storage;

    @BeanConstructor
    public ServiceD(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ServiceD.class.getSimpleName() + "[", "]")
                .add("\n")
                .add("name='" + name + "'")
                .add("\n")
                .add("service='" + userService + "'")
                .toString();
    }
}
