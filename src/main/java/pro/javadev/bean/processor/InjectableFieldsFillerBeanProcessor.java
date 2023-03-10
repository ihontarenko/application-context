package pro.javadev.bean.processor;

import pro.javadev.bean.BeanInjection;
import pro.javadev.bean.context.ApplicationContext;

import java.lang.reflect.Field;

public class InjectableFieldsFillerBeanProcessor implements BeanProcessor {

    @Override
    public void process(Object bean, ApplicationContext context) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(BeanInjection.class)) {
                try {
                    field.setAccessible(true);
                    field.set(bean, context.getBean(field.getType(), context.getFactory().getBeanName(field, BeanInjection.class)));
                } catch (IllegalAccessException e) {
                    // no-ops
                }
            }
        }
    }
}
