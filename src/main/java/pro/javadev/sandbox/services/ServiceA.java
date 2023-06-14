package pro.javadev.sandbox.services;

import pro.javadev.bean.BeanContructor;

import java.util.StringJoiner;

public class ServiceA implements ServiceInterface{

    private String name;

    @BeanContructor
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
