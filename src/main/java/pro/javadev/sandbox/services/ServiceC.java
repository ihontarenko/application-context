package pro.javadev.sandbox.services;

import pro.javadev.bean.BeanConstructor;

import java.util.StringJoiner;

public class ServiceC implements ServiceInterface {

    private String name;

    @BeanConstructor
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
