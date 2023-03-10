package pro.javadev.app.services;

import pro.javadev.bean.BeanContructor;
import pro.javadev.bean.Name;

import java.util.StringJoiner;

public class ServiceA implements ServiceInterface{

    private String name;

    @BeanContructor
    public ServiceA(@Name("NAME_OF_USER") String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ServiceA.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .toString();
    }
}
