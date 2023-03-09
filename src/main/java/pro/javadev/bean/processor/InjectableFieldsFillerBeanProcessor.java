package pro.javadev.bean.processor;

import pro.javadev.bean.Inject;
import pro.javadev.bean.context.ApplicationContext;

import java.lang.reflect.Field;

public class InjectableFieldsFillerBeanProcessor implements BeanProcessor {

    @Override
    public void process(Object bean, ApplicationContext context) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                try {
                    field.setAccessible(true);
                    field.set(bean, context.getBean(getBeanName(field)));
                } catch (IllegalAccessException e) {
                    // no-ops
                }
            }
        }
    }

    private String getBeanName(Field field) {
        String name = null;

        if (field.isAnnotationPresent(Inject.class)) {
            name = field.getAnnotation(Inject.class).value();
        }

        return name;
    }

}
