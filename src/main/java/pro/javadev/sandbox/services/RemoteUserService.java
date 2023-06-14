package pro.javadev.sandbox.services;

import pro.javadev.bean.BeanInjection;

public class RemoteUserService implements UserService {

    @BeanInjection("IN_MEMORY_USER_SERVICE")
    private UserService service;

    @Override
    public String toString() {
        return "RemoteUserService{" +
                "service=" + service +
                '}';
    }

}
