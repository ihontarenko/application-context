package pro.javadev.sandbox.services;

import pro.javadev.bean.BeanContructor;

import java.util.StringJoiner;

public class ServiceC implements ServiceInterface {

    private String name;

    @BeanContructor
    public ServiceC(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ServiceC.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .toString();
    }
}
