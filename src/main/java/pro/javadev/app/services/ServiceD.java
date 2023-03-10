package pro.javadev.app.services;

import pro.javadev.bean.BeanContructor;
import pro.javadev.bean.Inject;

import java.util.StringJoiner;

public class ServiceD implements ServiceInterface {

    private String name;

    @Inject
    private Storage storage;

    @Inject("REMOTE_USER_SERVICE")
    private UserService userService;

    @BeanContructor
    public ServiceD(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ServiceD.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("storage='" + storage + "'")
                .add("service='" + userService + "'")
                .toString();
    }
}