package pro.javadev.sandbox.services;

import pro.javadev.bean.BeanContructor;

import java.util.StringJoiner;

public class ServiceE implements ServiceInterface {

    private String name;

    @BeanContructor
    public ServiceE(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ServiceE.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .toString();
    }
}
