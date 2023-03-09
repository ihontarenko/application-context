package pro.javadev.app;

import pro.javadev.bean.Bean;
import pro.javadev.bean.Inject;

import java.util.StringJoiner;

@Bean("dummyUserService")
public class UserService2 {

    private UserService userService;

    @Inject
    public UserService2(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserService2.class.getSimpleName() + "[", "]")
                .add("userService=" + userService)
                .toString();
    }

}
