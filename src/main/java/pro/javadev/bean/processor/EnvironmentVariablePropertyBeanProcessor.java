package pro.javadev.bean.processor;

import pro.javadev.bean.PropertyValue;
import pro.javadev.bean.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentVariablePropertyBeanProcessor implements BeanProcessor {

    private final Map<Object, Object> globals;

    public EnvironmentVariablePropertyBeanProcessor(Map<?, ?>... maps) {
        Map<Object, Object> globals = new HashMap<>();

        for (Map<?, ?> map : maps) {
            globals.putAll(map);
        }

        this.globals = globals;
    }

    @Override
    public void process(Object bean, ApplicationContext context) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(PropertyValue.class)) {
                if (String.class.isAssignableFrom(field.getType())) {
                    PropertyValue variable = field.getAnnotation(PropertyValue.class);
                    try {
                        field.setAccessible(true);
                        field.set(bean, globals.get(variable.value()));
                    } catch (IllegalAccessException e) {
                        // no-ops
                    }
                } else {
                    throw new BeanProcessorException(
                            "FIELD ANNOTATED AS '" + PropertyValue.class + "' IN NOT " + String.class.getName());
                }
            }
        }
    }
}
