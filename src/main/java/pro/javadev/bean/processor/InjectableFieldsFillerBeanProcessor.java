package pro.javadev.bean.processor;

import pro.javadev.bean.BeanFactory;
import pro.javadev.bean.BeanInjection;
import pro.javadev.bean.context.ApplicationContext;

import java.lang.reflect.Field;

public class InjectableFieldsFillerBeanProcessor implements BeanProcessor {

    @Override
    public void process(Object bean, ApplicationContext context) {
        BeanFactory factory = context.getFactory();
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(BeanInjection.class)) {
                String beanName = factory.getBeanName(field, BeanInjection.class);
                Object value    = factory.getBean(field.getType(), beanName);
                try {
                    field.setAccessible(true);
                    field.set(bean, value);
                } catch (IllegalAccessException e) {
                    // no-ops
                }
            }
        }
    }
}
