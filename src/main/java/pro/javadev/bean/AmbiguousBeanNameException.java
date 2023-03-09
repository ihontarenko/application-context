package pro.javadev.bean;

import java.util.List;

public class AmbiguousBeanNameException extends RuntimeException {

    public AmbiguousBeanNameException(List<String> names, Class<?> type) {
        super("SPECIFY ACTUAL BEAN NAME ONE OF "+ names +" OF TYPE: " + type);
    }

}
