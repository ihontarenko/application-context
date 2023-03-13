package pro.javadev.app.services;

import pro.javadev.bean.BeanInjection;

public class InMemoryUserService implements UserService {

    @Override
    public String toString() {
        return "InMemoryUserService{" +
                "name='" + name + '\'' +
                '}';
    }

    @BeanInjection
    private String name;

}
