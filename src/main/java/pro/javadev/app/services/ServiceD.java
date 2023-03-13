package pro.javadev.app.services;

import pro.javadev.bean.BeanContructor;
import pro.javadev.bean.BeanInjection;

import java.util.StringJoiner;

public class ServiceD implements ServiceInterface {

    private String name;

    @BeanInjection("REMOTE_USER_SERVICE")
    private UserService userService;

//    @BeanInjection
    private Storage storage;

    @BeanContructor
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
